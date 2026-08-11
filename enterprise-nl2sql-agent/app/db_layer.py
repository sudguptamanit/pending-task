"""
Data-source access layer.

Wraps SQLAlchemy so the agent can:
  1. Introspect schema (tables/columns/types/FKs) to ground SQL generation.
  2. Execute *read-only* SQL safely, with row limits and timeouts.

Swapping the enterprise warehouse (Postgres, Snowflake, SQL Server, BigQuery
via SQLAlchemy dialect) requires changing only DATABASE_URL -- no code here
needs to change.
"""
import re
import time
from dataclasses import dataclass
from typing import Any

from sqlalchemy import create_engine, inspect, text
from sqlalchemy.engine import Engine

from app.config import settings

# Statements that must never reach the warehouse from an LLM-generated query.
_FORBIDDEN_PATTERN = re.compile(
    r"\b(insert|update|delete|drop|alter|truncate|grant|revoke|create|attach|pragma)\b",
    re.IGNORECASE,
)


@dataclass
class QueryResult:
    columns: list[str]
    rows: list[tuple]
    row_count: int
    truncated: bool
    elapsed_ms: float
    sql: str


class SQLGuardrailError(Exception):
    """Raised when a generated query fails safety validation."""


class EnterpriseDB:
    def __init__(self, database_url: str | None = None):
        self.engine: Engine = create_engine(
            database_url or settings.database_url,
            pool_pre_ping=True,
        )

    # ---------- Schema introspection ----------

    def get_schema_context(self, max_tables: int = 40) -> str:
        """
        Produces a compact, LLM-friendly description of the database schema:
        table names, columns with types, and foreign keys. This is injected
        into the SQL-generation prompt so the model grounds itself in the
        *actual* schema instead of hallucinating table/column names.
        """
        inspector = inspect(self.engine)
        lines = []
        table_names = inspector.get_table_names()[:max_tables]

        for table in table_names:
            cols = inspector.get_columns(table)
            col_desc = ", ".join(f"{c['name']} ({c['type']})" for c in cols)
            lines.append(f"TABLE {table}: {col_desc}")

            fks = inspector.get_foreign_keys(table)
            for fk in fks:
                if fk.get("constrained_columns") and fk.get("referred_table"):
                    lines.append(
                        f"  FK: {table}.{fk['constrained_columns']} -> "
                        f"{fk['referred_table']}.{fk['referred_columns']}"
                    )
        return "\n".join(lines)

    def get_sample_rows(self, table: str, limit: int = 3) -> str:
        """Small sample rows help the LLM understand data conventions (e.g. date format)."""
        try:
            with self.engine.connect() as conn:
                result = conn.execute(text(f"SELECT * FROM {table} LIMIT :lim"), {"lim": limit})
                cols = list(result.keys())
                rows = result.fetchall()
            return f"{table} sample: cols={cols} rows={rows}"
        except Exception:
            return ""

    # ---------- Safe execution ----------

    def validate_sql(self, sql: str) -> None:
        """
        Enforces a read-only policy. This is a defense-in-depth guardrail;
        production deployments should ALSO connect via a database role that
        only has SELECT grants, so this check is not the sole line of defense.
        """
        stripped = sql.strip().rstrip(";")
        if not stripped:
            raise SQLGuardrailError("Empty query.")
        if ";" in stripped:
            raise SQLGuardrailError("Multiple statements are not allowed.")
        if not re.match(r"^\s*(with|select)\b", stripped, re.IGNORECASE):
            raise SQLGuardrailError("Only SELECT / WITH (CTE) statements are permitted.")
        if _FORBIDDEN_PATTERN.search(stripped):
            raise SQLGuardrailError("Query contains a disallowed keyword (DML/DDL).")

    def _apply_row_limit(self, sql: str, limit: int) -> str:
        """Appends a LIMIT if the model didn't already bound the result set."""
        if re.search(r"\blimit\s+\d+\b", sql, re.IGNORECASE):
            return sql
        return f"{sql.rstrip().rstrip(';')} LIMIT {limit}"

    def run_query(self, sql: str) -> QueryResult:
        self.validate_sql(sql)
        bounded_sql = self._apply_row_limit(sql, settings.max_rows_returned)

        start = time.time()
        with self.engine.connect() as conn:
            conn = conn.execution_options(timeout=settings.query_timeout_seconds)
            result = conn.execute(text(bounded_sql))
            columns = list(result.keys())
            rows = result.fetchmany(settings.max_rows_returned + 1)

        elapsed_ms = (time.time() - start) * 1000
        truncated = len(rows) > settings.max_rows_returned
        rows = rows[: settings.max_rows_returned]

        return QueryResult(
            columns=columns,
            rows=[tuple(r) for r in rows],
            row_count=len(rows),
            truncated=truncated,
            elapsed_ms=round(elapsed_ms, 1),
            sql=bounded_sql,
        )


db = EnterpriseDB()

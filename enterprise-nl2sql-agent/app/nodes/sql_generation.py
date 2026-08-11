"""
Node: NL -> SQL generation (and self-correction retries).

Two-stage prompting:
  1. Ask the model to produce a brief reasoning plan (which tables/joins/
     aggregations are needed) -- this measurably improves SQL correctness
     on multi-table business questions vs. asking for SQL directly.
  2. Ask it to emit the final SQL only, grounded in the plan + schema.

On retries (triggered by validation or execution errors), the prior failed
SQL and the exact error are fed back in so the model can repair the query
instead of blindly regenerating.
"""
from langchain_core.messages import HumanMessage, SystemMessage

from app.config import get_llm
from app.state import AgentState

_PLAN_SYSTEM = """You are a senior data analyst planning how to answer a business \
question using SQL. Given the database schema and the business question, write a \
short (3-5 bullet) reasoning plan: which tables are needed, how they join, what \
filters/aggregations/time windows apply, and any ambiguity you're resolving with a \
reasonable default. Be concise. Do not write SQL yet."""

_SQL_SYSTEM = """You are an expert SQL analyst generating queries for an enterprise \
data warehouse (SQLite-compatible syntax by default; adjust if schema hints otherwise). \
Rules:
- Return ONLY the SQL query. No markdown fences, no commentary.
- SELECT-only. Never write INSERT/UPDATE/DELETE/DDL.
- Always alias aggregate columns clearly (e.g. SUM(revenue) AS total_revenue).
- Use explicit JOINs with the schema's foreign keys; never guess a join column that \
isn't in the schema.
- Add a sensible LIMIT for exploratory/listing queries unless the user asked for a \
single aggregate.
- Prefer readable, well-formatted SQL.
"""


def _build_schema_block(state: AgentState) -> str:
    return state.get("schema_context", "")


def plan_query(state: AgentState) -> dict:
    llm = get_llm(temperature=0.0)
    prompt = (
        f"Database schema:\n{_build_schema_block(state)}\n\n"
        f"Business question: {state['question']}"
    )
    response = llm.invoke([SystemMessage(content=_PLAN_SYSTEM), HumanMessage(content=prompt)])
    return {"plan": response.content.strip()}


def generate_sql(state: AgentState) -> dict:
    llm = get_llm(temperature=0.0)

    retry_context = ""
    if state.get("validation_error") or state.get("execution_error"):
        last_sql = (state.get("sql_history") or [""])[-1]
        error = state.get("validation_error") or state.get("execution_error")
        retry_context = (
            f"\n\nA previous attempt failed. Fix the query.\n"
            f"Previous SQL:\n{last_sql}\n"
            f"Error:\n{error}\n"
        )

    prompt = (
        f"Database schema:\n{_build_schema_block(state)}\n\n"
        f"Reasoning plan:\n{state.get('plan', '')}\n\n"
        f"Business question: {state['question']}"
        f"{retry_context}"
    )

    response = llm.invoke([SystemMessage(content=_SQL_SYSTEM), HumanMessage(content=prompt)])
    sql = response.content.strip()
    sql = sql.removeprefix("```sql").removeprefix("```").removesuffix("```").strip()

    history = state.get("sql_history", []) + [sql]
    attempts = state.get("attempts", 0) + 1

    return {
        "sql_query": sql,
        "sql_history": history,
        "attempts": attempts,
        "validation_error": None,
        "execution_error": None,
    }

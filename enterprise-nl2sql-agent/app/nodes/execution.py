"""
Node: SQL validation + safe execution.

Separated from generation so the graph can route failures back to
generate_sql for self-correction (see app/graph.py routing logic) without
re-running the reasoning-plan step every time.
"""
from dataclasses import asdict

from app.db_layer import db, SQLGuardrailError
from app.state import AgentState


def validate_and_execute(state: AgentState) -> dict:
    sql = state["sql_query"]

    try:
        db.validate_sql(sql)
    except SQLGuardrailError as e:
        return {"validation_error": str(e), "query_result": None}

    try:
        result = db.run_query(sql)
    except Exception as e:
        return {"execution_error": str(e), "query_result": None}

    return {
        "query_result": asdict(result),
        "validation_error": None,
        "execution_error": None,
    }

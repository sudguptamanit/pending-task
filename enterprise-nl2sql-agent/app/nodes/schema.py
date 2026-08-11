"""
Node: schema retrieval.

Pulls live schema (tables/columns/FKs) from the connected enterprise data
source so SQL generation is grounded in reality rather than the LLM's
assumptions. Cheap to call each turn since introspection is a lightweight
metadata query, but could be cached with a TTL for very large warehouses.
"""
from app.db_layer import db
from app.state import AgentState


def retrieve_schema(state: AgentState) -> dict:
    schema_context = db.get_schema_context()
    return {"schema_context": schema_context}

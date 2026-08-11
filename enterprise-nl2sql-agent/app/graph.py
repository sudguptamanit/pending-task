"""
Graph assembly: the agentic workflow.

Flow:

  classify_intent
      |-- chitchat        -> handle_chitchat -> END
      |-- clarify         -> ask_clarification -> END
      '-- data_query      -> retrieve_schema -> plan_query -> generate_sql
                                                                   |
                                                          validate_and_execute
                                                                   |
                                        success? --------------- / \\ --------------- failure?
                                       generate_insights                    attempts < max?
                                             |                              /          \\
                                            END                  generate_sql (retry)   handle_execution_failure -> END

This gives us: multi-step reasoning (plan -> SQL -> execute -> insights),
a bounded self-correction loop (LangGraph conditional edge back to
generate_sql), and context retention across turns via the MemorySaver
checkpointer keyed on a conversation thread_id.
"""
from langgraph.graph import StateGraph, START, END
from langgraph.checkpoint.memory import MemorySaver

from app.state import AgentState
from app.config import settings

from app.nodes.intent import classify_intent
from app.nodes.schema import retrieve_schema
from app.nodes.sql_generation import plan_query, generate_sql
from app.nodes.execution import validate_and_execute
from app.nodes.insights import generate_insights
from app.nodes.conversation import (
    ask_clarification,
    handle_chitchat,
    handle_execution_failure,
)


def _route_after_intent(state: AgentState) -> str:
    intent = state.get("intent", "data_query")
    if intent == "chitchat":
        return "chitchat"
    if intent == "clarify":
        return "clarify"
    return "data_query"


def _route_after_execution(state: AgentState) -> str:
    has_error = bool(state.get("validation_error") or state.get("execution_error"))
    if not has_error:
        return "success"
    if state.get("attempts", 0) >= settings.max_self_correct_attempts:
        return "give_up"
    return "retry"


def build_graph():
    graph = StateGraph(AgentState)

    graph.add_node("classify_intent", classify_intent)
    graph.add_node("retrieve_schema", retrieve_schema)
    graph.add_node("plan_query", plan_query)
    graph.add_node("generate_sql", generate_sql)
    graph.add_node("validate_and_execute", validate_and_execute)
    graph.add_node("generate_insights", generate_insights)
    graph.add_node("ask_clarification", ask_clarification)
    graph.add_node("handle_chitchat", handle_chitchat)
    graph.add_node("handle_execution_failure", handle_execution_failure)

    graph.add_edge(START, "classify_intent")

    graph.add_conditional_edges(
        "classify_intent",
        _route_after_intent,
        {
            "chitchat": "handle_chitchat",
            "clarify": "ask_clarification",
            "data_query": "retrieve_schema",
        },
    )

    graph.add_edge("retrieve_schema", "plan_query")
    graph.add_edge("plan_query", "generate_sql")
    graph.add_edge("generate_sql", "validate_and_execute")

    graph.add_conditional_edges(
        "validate_and_execute",
        _route_after_execution,
        {
            "success": "generate_insights",
            "retry": "generate_sql",           # self-correction loop
            "give_up": "handle_execution_failure",
        },
    )

    graph.add_edge("generate_insights", END)
    graph.add_edge("ask_clarification", END)
    graph.add_edge("handle_chitchat", END)
    graph.add_edge("handle_execution_failure", END)

    # MemorySaver checkpoints full AgentState per thread_id, giving the agent
    # conversational memory across turns (follow-ups, "compare that to...", etc.)
    # Swap for a persistent checkpointer (e.g. Postgres/Redis-backed) in production.
    checkpointer = MemorySaver()
    return graph.compile(checkpointer=checkpointer)


agent_app = build_graph()

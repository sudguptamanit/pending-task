"""
Shared state passed between LangGraph nodes.

LangGraph threads this TypedDict through every node; each node reads what it
needs and returns a partial update that gets merged in. Conversation history
(`messages`) is checkpointed automatically per-thread so follow-up questions
retain context (see app/graph.py MemorySaver usage).
"""
from typing import Annotated, Any, Optional, TypedDict
from langgraph.graph.message import add_messages


class AgentState(TypedDict, total=False):
    # Conversation
    messages: Annotated[list, add_messages]   # full chat history (HumanMessage/AIMessage)
    question: str                              # latest user question, possibly rewritten
    original_question: str                     # verbatim user input

    # Reasoning / planning
    intent: str                                 # "data_query" | "clarify" | "chitchat"
    plan: str                                    # brief natural-language reasoning plan
    schema_context: str                          # injected DB schema description

    # NL -> SQL
    sql_query: str
    sql_history: list[str]                       # all attempted SQL versions (for self-correction)
    validation_error: Optional[str]

    # Execution
    query_result: Optional[dict[str, Any]]        # serialized QueryResult
    execution_error: Optional[str]
    attempts: int

    # Output
    insights: str                                 # business-language summary
    final_answer: str
    needs_clarification: bool
    clarification_question: Optional[str]

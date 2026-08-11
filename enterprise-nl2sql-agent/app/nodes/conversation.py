"""
Nodes for the non-data-query branches of the graph: asking for clarification
when a question is too ambiguous to safely turn into SQL, and handling
plain conversational turns (greetings, thanks, etc.) without touching the
data warehouse.
"""
from langchain_core.messages import HumanMessage, SystemMessage

from app.config import get_llm
from app.state import AgentState


def ask_clarification(state: AgentState) -> dict:
    question = state.get("clarification_question") or (
        "Could you clarify what metric, time period, or scope you're interested in?"
    )
    return {"final_answer": question}


def handle_chitchat(state: AgentState) -> dict:
    llm = get_llm(temperature=0.4)
    system = (
        "You are a helpful enterprise data assistant for company leadership. "
        "Respond briefly and warmly to this non-data message, and remind the user "
        "you're ready to answer questions about company data (sales, revenue, "
        "customers, regions, products, etc.)."
    )
    response = llm.invoke([SystemMessage(content=system), HumanMessage(content=state["original_question"])])
    return {"final_answer": response.content.strip()}


def handle_execution_failure(state: AgentState) -> dict:
    """Reached only if self-correction attempts are exhausted."""
    error = state.get("execution_error") or state.get("validation_error") or "Unknown error"
    msg = (
        "I wasn't able to produce a reliable answer to that after a few attempts. "
        f"The last issue was: {error}\n\n"
        "Could you rephrase the question, or narrow it down (e.g. specific time range, "
        "region, or metric)?"
    )
    return {"final_answer": msg}

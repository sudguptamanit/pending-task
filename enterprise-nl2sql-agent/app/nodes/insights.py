"""
Node: business insight extraction.

Converts raw rows into an executive-ready narrative: headline finding,
supporting numbers, and (where relevant) a suggested follow-up angle.
This is the "leadership-facing" layer that differentiates the assistant
from a raw SQL runner.
"""
from langchain_core.messages import HumanMessage, SystemMessage

from app.config import get_llm
from app.state import AgentState

_SYSTEM = """You are briefing a company executive on a data query result. \
Write a concise, decision-oriented answer:
- Lead with the direct answer to their question (1-2 sentences).
- Follow with 1-3 supporting bullet points (key numbers, trends, standouts).
- If relevant, suggest one natural follow-up question they might want to ask next.
- Do not mention SQL, tables, or technical implementation details.
- Be precise with numbers; do not invent figures beyond what's in the data provided.
- If the result set is empty, say so plainly and suggest why (e.g. filters too narrow).
"""


def generate_insights(state: AgentState) -> dict:
    llm = get_llm(temperature=0.3)
    result = state.get("query_result") or {}
    columns = result.get("columns", [])
    rows = result.get("rows", [])
    truncated = result.get("truncated", False)

    data_preview = f"Columns: {columns}\nRows ({len(rows)} shown{' , truncated' if truncated else ''}): {rows[:50]}"

    prompt = (
        f"Business question: {state['question']}\n\n"
        f"Query result:\n{data_preview}"
    )
    response = llm.invoke([SystemMessage(content=_SYSTEM), HumanMessage(content=prompt)])
    insights = response.content.strip()

    return {"insights": insights, "final_answer": insights}

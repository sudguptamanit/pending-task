"""
Node: intent classification & follow-up resolution.

Leadership conversations are rarely single-shot: "How did APAC do last
quarter?" -> "Break that down by product" is a follow-up that depends on
prior turns. This node uses the running message history to:
  1. Rewrite the latest question into a fully self-contained query
     (resolving pronouns like "that", "them", "the same period").
  2. Classify intent so chit-chat / vague requests don't hit the SQL engine.
"""
import json
from langchain_core.messages import HumanMessage, SystemMessage

from app.config import get_llm
from app.state import AgentState

_SYSTEM = """You are the intent router for an enterprise data assistant used by \
company leadership. Given the conversation so far and the latest user message, do two things:

1. Rewrite the latest user message into a fully self-contained question that \
resolves any references to prior turns (e.g. "that region", "same period", \
"those customers"). If it's already self-contained, return it unchanged.
2. Classify intent as one of:
   - "data_query": requires querying enterprise data to answer
   - "clarify": too ambiguous to safely turn into a query (missing metric, \
timeframe, or scope, in a way that would change the SQL significantly)
   - "chitchat": greeting, thanks, or general question not requiring data

Respond ONLY with strict JSON, no markdown fences:
{"rewritten_question": "...", "intent": "data_query|clarify|chitchat", \
"clarification_question": "..." (only if intent is "clarify", else null)}
"""


def classify_intent(state: AgentState) -> dict:
    llm = get_llm(temperature=0.0)
    history = state.get("messages", [])[-8:]  # bounded window for context retention
    history_text = "\n".join(
        f"{m.type}: {m.content}" for m in history if hasattr(m, "content")
    )

    prompt = (
        f"Conversation so far:\n{history_text}\n\n"
        f"Latest user message: {state['original_question']}"
    )

    response = llm.invoke([SystemMessage(content=_SYSTEM), HumanMessage(content=prompt)])
    raw = response.content.strip()
    raw = raw.removeprefix("```json").removeprefix("```").removesuffix("```").strip()

    try:
        parsed = json.loads(raw)
    except json.JSONDecodeError:
        # Fail safe: treat as a direct data query using the verbatim question.
        parsed = {
            "rewritten_question": state["original_question"],
            "intent": "data_query",
            "clarification_question": None,
        }

    return {
        "question": parsed.get("rewritten_question") or state["original_question"],
        "intent": parsed.get("intent", "data_query"),
        "needs_clarification": parsed.get("intent") == "clarify",
        "clarification_question": parsed.get("clarification_question"),
    }

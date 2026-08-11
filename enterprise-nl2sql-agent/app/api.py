"""
FastAPI wrapper exposing the agent over HTTP for integration into internal
tools (Slack/Teams bots, executive dashboards, BI portals).

POST /chat
{
  "message": "How did APAC revenue trend last quarter?",
  "thread_id": "optional-existing-conversation-id"
}

Response includes the natural-language answer, the SQL used (for
transparency/auditability), and the thread_id to send on follow-up calls.
"""
import uuid
from fastapi import FastAPI
from pydantic import BaseModel
from langchain_core.messages import HumanMessage, AIMessage

from app.graph import agent_app

api = FastAPI(title="Enterprise NL-to-SQL Agent")


class ChatRequest(BaseModel):
    message: str
    thread_id: str | None = None


class ChatResponse(BaseModel):
    answer: str
    sql_query: str | None = None
    thread_id: str
    row_count: int | None = None
    error: str | None = None


@api.post("/chat", response_model=ChatResponse)
def chat(req: ChatRequest):
    thread_id = req.thread_id or str(uuid.uuid4())
    config = {"configurable": {"thread_id": thread_id}}

    result = agent_app.invoke(
        {
            "messages": [HumanMessage(content=req.message)],
            "original_question": req.message,
            "attempts": 0,
        },
        config=config,
    )

    answer = result.get("final_answer", "I wasn't able to generate an answer.")
    agent_app.update_state(config, {"messages": [AIMessage(content=answer)]})

    query_result = result.get("query_result") or {}
    return ChatResponse(
        answer=answer,
        sql_query=result.get("sql_query"),
        thread_id=thread_id,
        row_count=query_result.get("row_count"),
        error=result.get("execution_error") or result.get("validation_error"),
    )


@api.get("/health")
def health():
    return {"status": "ok"}

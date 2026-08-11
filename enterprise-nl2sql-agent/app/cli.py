"""
Simple interactive CLI so leadership (or a dev testing the agent) can chat
with the assistant directly in the terminal. Conversation state persists
for the life of the process via a fixed thread_id, enabling natural
follow-ups ("break that down by region", "and last quarter?").
"""
import uuid
from langchain_core.messages import HumanMessage, AIMessage
from rich.console import Console
from rich.markdown import Markdown
from rich.panel import Panel

from app.graph import agent_app

console = Console()


def run_cli():
    thread_id = str(uuid.uuid4())
    config = {"configurable": {"thread_id": thread_id}}

    console.print(Panel.fit(
        "[bold]Enterprise Data Assistant[/bold]\n"
        "Ask questions about company data in plain English. Type 'exit' to quit.",
        border_style="cyan",
    ))

    while True:
        try:
            user_input = console.input("\n[bold green]You:[/bold green] ")
        except (KeyboardInterrupt, EOFError):
            break
        if user_input.strip().lower() in {"exit", "quit"}:
            break
        if not user_input.strip():
            continue

        result = agent_app.invoke(
            {
                "messages": [HumanMessage(content=user_input)],
                "original_question": user_input,
                "attempts": 0,
            },
            config=config,
        )

        answer = result.get("final_answer", "I wasn't able to generate an answer.")
        # Persist the answer into message history for future context.
        agent_app.update_state(config, {"messages": [AIMessage(content=answer)]})

        console.print("\n[bold blue]Assistant:[/bold blue]")
        console.print(Markdown(answer))

        if result.get("sql_query") and not result.get("execution_error"):
            console.print(f"\n[dim]SQL used: {result['sql_query']}[/dim]")


if __name__ == "__main__":
    run_cli()

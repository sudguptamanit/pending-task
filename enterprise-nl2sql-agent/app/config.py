"""
Central configuration for the enterprise NL-to-SQL agent.

All environment-specific values (LLM provider/model, database connection,
safety limits) are read here so the rest of the codebase stays
environment-agnostic and easy to promote from dev -> staging -> prod.
"""
import os
from dataclasses import dataclass
from dotenv import load_dotenv

load_dotenv()


@dataclass(frozen=True)
class Settings:
    # LLM
    llm_model: str = os.getenv("LLM_MODEL", "claude-sonnet-4-5")
    anthropic_api_key: str = os.getenv("ANTHROPIC_API_KEY", "")

    # Data source
    database_url: str = os.getenv(
        "DATABASE_URL", "sqlite:///data/enterprise_demo.db"
    )
    allowed_schemas: tuple = tuple(
        s.strip() for s in os.getenv("ALLOWED_SCHEMAS", "public").split(",")
    )

    # Safety / performance guardrails
    max_rows_returned: int = int(os.getenv("MAX_ROWS_RETURNED", "200"))
    query_timeout_seconds: int = int(os.getenv("QUERY_TIMEOUT_SECONDS", "30"))
    max_self_correct_attempts: int = int(
        os.getenv("MAX_SELF_CORRECT_ATTEMPTS", "3")
    )


settings = Settings()


def get_llm(temperature: float = 0.0):
    """
    Factory for the chat model used throughout the graph.
    Swap this to langchain_openai.ChatOpenAI or another provider without
    touching any node logic -- everything downstream depends only on the
    LangChain BaseChatModel interface.
    """
    from langchain_anthropic import ChatAnthropic

    return ChatAnthropic(
        model=settings.llm_model,
        temperature=temperature,
        api_key=settings.anthropic_api_key or None,
    )

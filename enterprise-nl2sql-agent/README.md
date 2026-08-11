# Enterprise NL-to-SQL Agent

An agentic AI system that lets leadership ask business questions in plain
English and get back accurate, decision-ready answers — grounded in your
actual enterprise data. Built with **LangGraph** for multi-step reasoning
and **LangChain** for LLM/tool orchestration.

## Architecture

```
User question
     │
     ▼
classify_intent   ── rewrites follow-ups into standalone questions using
     │                conversation history; routes to chitchat / clarify / data_query
     ├── chitchat ──────────────► handle_chitchat ──► END
     ├── clarify  ──────────────► ask_clarification ──► END
     └── data_query
              │
              ▼
       retrieve_schema   ── live introspection of the connected warehouse
              │
              ▼
        plan_query        ── LLM reasoning plan: tables, joins, filters, aggregations
              │
              ▼
        generate_sql       ── LLM emits SELECT-only SQL grounded in schema + plan
              │
              ▼
   validate_and_execute    ── guardrails (read-only, single statement, LIMIT,
              │                timeout) then runs against the warehouse
       success │  \\ error (retry loop, bounded by MAX_SELF_CORRECT_ATTEMPTS)
              │    \\________________________►  back to generate_sql
              ▼
       generate_insights   ── turns rows into an executive narrative
              │
              ▼
             END
```

Conversation state (including full message history) is checkpointed per
`thread_id` via LangGraph's `MemorySaver`, so follow-ups like *"break that
down by product"* or *"and how does that compare to last quarter?"* work
naturally without leadership having to restate context.

## Key design decisions

- **Read-only by construction**: generated SQL is validated against a
  denylist (INSERT/UPDATE/DELETE/DDL/multi-statement) before execution, and
  row counts/timeouts are enforced. In production, pair this with a
  database role that only grants `SELECT`.
- **Plan-then-SQL prompting**: the agent reasons about tables/joins/filters
  in natural language *before* writing SQL — this reduces join errors and
  hallucinated columns on multi-table business questions.
- **Bounded self-correction loop**: failed queries (syntax errors, bad
  columns) are fed back to `generate_sql` with the exact error, up to
  `MAX_SELF_CORRECT_ATTEMPTS`, before gracefully asking the user to
  rephrase — never silently fails.
- **Warehouse-agnostic**: all DB access goes through SQLAlchemy
  (`app/db_layer.py`). Point `DATABASE_URL` at Postgres, Snowflake, SQL
  Server, BigQuery, etc. — no other code changes needed.
- **Two integration surfaces**: a terminal CLI (`app/cli.py`) for quick use
  and testing, and a FastAPI service (`app/api.py`) for embedding into
  Slack/Teams bots, internal dashboards, or BI portals.

## Project layout

```
app/
  config.py            # settings + LLM factory (swap providers here)
  db_layer.py           # SQLAlchemy access, schema introspection, guardrails
  state.py               # LangGraph AgentState schema
  graph.py                # graph assembly, routing, self-correction loop
  cli.py                   # interactive terminal chat
  api.py                    # FastAPI /chat endpoint
  nodes/
    intent.py            # follow-up resolution + intent routing
    schema.py             # schema retrieval node
    sql_generation.py      # plan + NL-to-SQL generation
    execution.py            # validation + safe execution
    insights.py              # business-language summarization
    conversation.py           # clarify / chitchat / failure handling
data/
  seed_demo_db.py        # builds a runnable demo SQLite warehouse
requirements.txt
.env.example
```

## Setup

```bash
pip install -r requirements.txt
cp .env.example .env          # add your ANTHROPIC_API_KEY
python data/seed_demo_db.py   # builds a demo warehouse (sales/customers/products)
```

### Run the CLI

```bash
python -m app.cli
```

```
You: How did revenue trend by region last quarter?
Assistant: APAC led with the highest quarterly revenue, driven largely by
Cloud Analytics Suite and Enterprise CRM sales...
 - APAC: $412K (+18% vs prior quarter)
 - North America: $298K
 - EMEA: $265K
Would you like a breakdown by product category next?

SQL used: SELECT r.region_name, SUM(s.revenue) AS total_revenue ...
```

### Run the API

```bash
uvicorn app.api:api --reload
```

```bash
curl -X POST localhost:8000/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Who are our top 5 customers by revenue this year?"}'
```

## Connecting to a real enterprise warehouse

Update `.env`:

```
DATABASE_URL=postgresql+psycopg2://user:pass@host:5432/warehouse
ALLOWED_SCHEMAS=analytics,finance
```

Install the relevant driver (see commented options in `requirements.txt`).
No other code changes are required — schema introspection, SQL generation,
and guardrails all operate through the same SQLAlchemy interface.

## Extending this into a full solution

- **Enterprise API integration**: add LangChain tools alongside SQL (e.g. a
  Salesforce or ERP API tool) and let `plan_query`/`generate_sql` become a
  proper LangGraph *tool-calling* agent choosing between SQL and API calls.
- **Row/column-level security**: inject the requesting user's role into the
  schema/plan prompts and filter `retrieve_schema` to only expose tables/
  columns they're entitled to see.
- **Caching**: cache `get_schema_context()` with a TTL for large warehouses;
  cache repeated question→SQL mappings.
- **Observability**: log `plan`, `sql_history`, `attempts`, and `elapsed_ms`
  per turn (already present in state) to a metrics store to track accuracy
  and latency over time.
- **Persistent checkpointer**: swap `MemorySaver` for a Postgres/Redis-backed
  LangGraph checkpointer so conversations survive process restarts.

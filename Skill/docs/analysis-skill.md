# Analysis Skill

Purpose
- Rapidly assess feasibility and risk, outline a minimal technical spike, and summarize key effort drivers and risks—grounded in existing repository prior art and dependency/license scan outputs.

Scope
- Default read-only across repos/scans; writes occur as comments/drafts/PRs with explicit human approval for any risk-bearing change.

Key Capabilities
- Feasibility and risk assessment (evidence-based, with assumptions and blockers).
- Quick tech spike outline (timeboxed plan with success criteria and instrumentation).
- Effort drivers and risks summarizer (ranked, with mitigations).
- Repo scanning for prior art and summarization.
- Dependency/license scan summaries (SCA/SBOM ingestion → concise, actionable digest).

Primary Integrations
- VCS: GitHub/GitLab/Bitbucket (code search, repo read, PR comment drafts).
- Scanners: Software composition analysis (SCA) and license scanners (JSON/SARIF/SBOM ingest).
- Storage: Vector index for semantic prior-art retrieval; lightweight KV/DB for metrics.

Guardrails
- Evidence-only assertions with citations; if uncertain, mark as "Unknown" and list missing info.
- Least-privilege tokens; redact secrets/PII; all writes logged with correlation IDs.
- No license advice beyond summarization; defer legal conclusions to humans.

--------------------------------------------------------------------------------

## 1) Workflows

1. Feasibility & Risk Assessment
   - Input: Problem statement, constraints, target stack(s), non-functionals (e.g., latency/SLA), and deadline.
   - Process:
     - Retrieve prior art from repos (semantic/code search).
     - Ingest latest dependency/license findings (JSON/SBOM).
     - Identify unknowns and high-risk areas; map to feasibility dimensions (technical, operational, legal).
   - Output: Verdict (Feasible | Feasible-with-risks | Not-feasible | Unknown), assumptions, blockers, top-N risks (RAG), and next actions. Includes citations.

       	```
       	Feasibility Verdict: Feasible-with-risks
       	Assumptions:
       	- Redis available with >= 3-node cluster
       	- Payment PSP supports idempotency keys
       	Blockers:
       	- Missing PCI logging review
       	Top Risks:
       	- Rate-limit edge cases (Red): spike redis-leaky-bucket
       	- Third-party SLA variance (Amber): add circuit breaker + backoff
       	Citations: [repo://service/rate_limiter.go#L40-L92], [scan://snyk/json#pkg/redis@4.6.0]
       	Next Actions (timebox 2d): run spike; confirm PSP idempotency; review logs with Security.
       	```

2. Quick Tech Spike Outline
   - Input: Hypothesis to validate + constraints + acceptance criteria draft.
   - Process: Propose minimal approach, scope, test-of-feasibility, env/setup, and exit criteria; identify observability needs.
   - Output: Timeboxed spike plan (tasks, owners optional, dependencies, risks).

       	```
       	Spike: Async Order Confirmation Queue
       	Hypothesis: SQS + worker can process 1k msgs/min with p95 < 200ms
       	Timebox: 2 days
       	Plan:
       	- Provision queue + minimal worker (Go)
       	- Synthetic load (1k/min) with jitter
       	- Instrument p95, DLQ count, retries
       	Exit Criteria:
       	- p95 < 200ms, DLQ < 0.1%, zero auth errors
       	Risks/Mitigations:
       	- Throttling → backoff+jitter, concurrency caps
       	Artifacts:
       	- Repo branch, dashboard link, summary comment
       	```

3. Effort Drivers & Risks Summarizer
   - Input: Epic/story set or brief feature description.
   - Process: Extract drivers (e.g., external APIs, data migration, perf/SLA, security/compliance, algorithmic complexity, environments), rank by uncertainty/impact, attach mitigations.
   - Output: Ranked list with short rationale and suggested sequencing.

       	```
       	Effort Drivers (Ranked):
       	1) Data migration volume (High) — unknown data shape → add profiling task
       	2) Third-party API variance (Med) — sandbox instability → contract tests
       	3) Perf SLA p95<=200ms (Med) — caching plan + perf budget
       	Risks:
       	- License conflict: GPL transitive dep → seek alt or dual-license
       	- Security: JWT audience mismatch → validation middleware
       	```

4. Repo Prior Art Scan
   - Input: Keywords/components (e.g., "circuit breaker", "leaky bucket", "oauth2").
   - Process: Code and semantic search; deduplicate; summarize usage patterns and reusability; cite files/lines/commits.
   - Output: Reusable modules/snippets with reuse risk notes.

       	```
       	Prior Art Candidates:
       	- pkg/rate/limiter.go (reusable 70%) — missing burst config
       	- pkg/http/retry_mw.go (reusable 60%) — add idempotency predicate
       	References: [repo://service/limiter.go#L20-L88], [repo://gateway/retry_mw.go#L10-L64]
       	```

5. Dependency/License Scan Summaries
   - Input: SCA/SBOM JSON or SARIF (from your toolchain).
   - Process: Parse vulns (severity, EPSS/CVSS where present), licenses (type, obligations, conflicts), group by fix strategy.
   - Output: Terse digest + recommended actions; no legal advice.

       	```json
       	{
       	  "deps_at_risk": 3,
       	  "vulns": [
       	    {"pkg":"openssl","version":"1.1.1u","severity":"High","fix":"1.1.1w"},
       	    {"pkg":"lodash","version":"4.17.19","severity":"Med","fix":"4.17.21"}
       	  ],
       	  "licenses": [
       	    {"pkg":"libA","license":"MIT","action":"None"},
       	    {"pkg":"libB","license":"GPL-3.0","action":"Review/Replace"}
       	  ]
       	}
       	```

--------------------------------------------------------------------------------

## 2) Integrations

- VCS
  - Read: repo list, code search, file blobs; optional semantic index.
  - Write (gated): PR comments with feasibility verdicts and spike outlines; links to files/lines.
- Scanners
  - Accept JSON/SARIF/SBOM from your SCA/license tools; store last-seen results; diff new vs prior for deltas.
- Storage/Index
  - Vector store for code/doc chunks; KV/DB for metrics (timestamps, estimates, actuals).

Configuration (env vars)
- VCS_HOST, VCS_TOKEN
- SCAN_BUCKET or SCAN_API, SCAN_TOKEN
- INDEX_STORE, MODEL_NAME, TOP_K (default: 5)
- METRICS_STORE

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- Time-to-Feasibility (TTF)
  - Definition: time between analysis start and first evidence-backed feasibility verdict.
  - Collection: record start_time when analysis task begins; record feasible_time on first posted verdict.
       	```json
       	{"item":"EPIC-42","start_time":"2026-08-10T09:10:00Z","feasible_time":"2026-08-10T16:05:00Z","ttf_hours":6.92}
       	```

- Forecast Accuracy Delta
  - Definition: |initial_estimate - actual| / initial_estimate (per spike/feature).
  - Collection: read initial estimate from spike outline; actual from time logs or PR merge deltas.
       	```json
       	{"item":"SPIKE-redis-bucket","initial_estimate_h":16,"actual_h":20,"delta":0.25}
       	```

Reporting
- Per sprint rollup with trend; export CSV/Confluence as needed.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Tokens scoped read-most; comment-only writes; PR merges or ticket edits require human approval.
- Audit: structured logs with correlationId, input hashes, source citations.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Feasibility assessor (evidence-only)
       	```
       	"You are an analysis assistant. Use only retrieved repo/code/scan evidence. Output: Verdict, Assumptions, Blockers, Top Risks (RAG), Next Actions, Citations. If unsure, say 'Unknown' and list missing info."
       	```

- Spike outline
       	```
       	Fields: Hypothesis, Timebox, Plan (3–6 bullets), Instrumentation, Exit Criteria, Risks/Mitigations, Artifacts.
       	```

- Effort drivers checklist
       	```
       	Drivers: External APIs, Data migration, Perf/SLA, Security/Compliance, Algorithmic complexity, Multi-env/instrumentation, Operational runbooks.
       	```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Read-only dry runs (2 sprints) → enable PR comment mode → gated updates to tickets/docs.
- Validate with gold-set analyses; track TTF and forecast deltas improvement thresholds before widening scope.
# Operations & Maintenance Skill

Purpose
- Reduce incident impact and toil by acting as an incident copilot (triage logs/traces/metrics, propose probable causes), retrieving runbooks and composing safe, auditable command plans, drafting RCAs, and routing/de-duplicating tickets. Integrates observability stacks, on-call systems, and CMDB/KB to create postmortem docs and improve knowledge reuse.

Scope
- Read: observability (logs/metrics/traces), on-call schedules/incidents, CMDB/service maps, KB/runbooks, release/change feeds.
- Write (gated): comments/drafts for incident summaries, command plans, RCA/postmortems, and ticket routing/de-dup suggestions. Any live command execution is human-approved and out-of-band.

Key Capabilities
- Incident copilot: correlate alerts/logs/traces; summarize impact; propose probable-cause hypotheses with confidence.
- Runbook retrieval + safe, auditable command composition (dry-run, blast-radius checks, approvals).
- RCA draft (timeline, 5 Whys, corrective actions).
- Ticket routing and de-duplication using service ownership and signal correlation.
- Postmortem document creation with action items and owners; knowledge capture linking.

Primary Integrations
- Observability: logs (e.g., Elasticsearch/Loki), metrics (Prometheus/Cloud), traces (OTel/Jaeger).
- On-call: PagerDuty/Opsgenie (incidents, schedules, notes).
- CMDB/KB: ServiceNow/Confluence (service map, runbooks, KB articles).
- Change feeds: VCS/CI/CD events, feature flags, infra changes (for correlation).
- Docs export: Markdown to Confluence or repo PRs.

Guardrails
- No direct prod command execution by default. All command plans require human approval and are recorded with correlationId, author, and timestamp.
- Redaction of secrets/PII in all outputs; least-privilege tokens; read-most access by default.
- Evidence-first: every hypothesis cites sources (alerts, log queries, traces) with links/time windows; uncertainty explicitly called out.

--------------------------------------------------------------------------------

## 1) Workflows

1) Incident Copilot: Log/Trace Triage → Probable Cause
- Input: Alert payload(s), time window, impacted service(s).
- Process: Correlate metrics anomalies with error spikes; sample logs; fetch top spans by error/latency; cluster recent deploys/feature-flag changes; generate hypotheses.
- Output: Incident summary + hypotheses with confidence and evidence links.

    ```json
    {
      "incident": "INC-2451",
      "service": "checkout-api",
      "summary": "Elevated 5xx and latency in checkout-api starting 09:12Z; scope EU region; spike in POST /payments",
      "hypotheses": [
        {
          "desc": "DB connection pool exhaustion after deploy 2026.08.3",
          "confidence": 0.72,
          "evidence": {
            "metrics": ["p95 +60% (180→288ms)", "5xx 0.2%→1.6%"],
            "logs_query": "trace_id:error AND route:/payments",
            "traces": ["slow span: sql.query payments#authz 450ms"],
            "changes": ["deploy: checkout-api@2026.08.3 09:05Z"]
          }
        },
        {
          "desc": "PSP timeout: upstream latency regression",
          "confidence": 0.41,
          "evidence": {"metrics": ["PSP p95 +120ms"], "flags": ["payments.psp_retry=true"]}
        }
      ],
      "recommended_next": ["scale pool +5 temporarily", "roll back feature flag", "run connection leak check"]
    }
    ```

2) Runbook Retrieval + Safe, Auditable Command Composition
- Input: Target service/alert signature; runbook/KB index; environment (prod/stage).
- Process: Retrieve relevant runbook steps; synthesize a Command Plan with pre-checks, dry-run, blast-radius estimate, rollback, and explicit approval steps.
- Output: Command Plan (no execution), with redacted parameters and validation checklist.

    ```
    Command Plan: checkout-api pool pressure (env: prod, region: eu-west-1)
    Pre-checks:
    - Confirm error budget remaining > 50% (link)
    - Verify no concurrent migrations (deploy job #813 ok)
    Dry-run:
    - kubectl --context=prod-eu get hpa checkout-api --namespace=payments
    Blast Radius:
    - Affects 6 replicas, single namespace; no cross-service dependency change
    Actions (upon approval):
    1) kubectl --context=prod-eu -n payments scale deploy/checkout-api --replicas=10
    2) kubectl --context=prod-eu -n payments rollout status deploy/checkout-api --timeout=5m
    Rollback:
    - scale back to replicas=6; verify p95 and error rate return to baseline
    Approvals Required:
    - On-call primary + SRE duty (2-eyes)
    Notes:
    - Redacted env vars; commands logged with correlationId=INC-2451#cmd-001
    ```

3) Ticket Routing and De-dup
- Input: Incoming alerts/incidents with fingerprints (service, region, error code, route), CMDB ownership.
- Process: Correlate duplicates within time window; pick primary; link/merge; assign owner based on service map and on-call schedule.
- Output: Routing/merge suggestion.

    ```json
    {
      "primary": "INC-2451",
      "duplicates": ["ALERT-9091","ALERT-9093"],
      "reason": ["same service=checkout-api", "region=eu-west-1", "fingerprint=POST:/payments 5xx"],
      "owner": {"team": "Payments", "on_call": "@alice"},
      "action": "merge-and-assign",
      "notify_channels": ["#oncall-payments"]
    }
    ```

4) RCA Draft (Timeline + 5 Whys + Corrective Actions)
- Input: Incident artifacts (alerts, notes, change events, chat transcripts), resolved state.
- Process: Build timeline; perform 5 Whys; classify contributing factors (people/process/tech); propose corrective and preventive actions with owners/dates.
- Output: RCA draft markdown.

    ```
    # RCA Draft — INC-2451
    Impact: 1.6% checkout failures, 25 min; EU-only
    Timeline:
    - 09:05Z deploy 2026.08.3
    - 09:12Z alert fired (5xx)
    - 09:20Z scale out; 09:37Z stabilized
    5 Whys:
    1) Why failures? → DB pool saturation after deploy
    2) Why saturation? → new ORM setting increased per-request connections
    3) Why setting changed? → default changed in upgrade; not pinned
    4) Why not caught? → missing load test for connection usage
    5) Why missing? → no perf budget gate in CI
    Actions:
    - Pin ORM pool settings (Owner: Backend, due: 2026-08-17)
    - Add perf test for connection usage (Owner: QA, due: 2026-08-24)
    - CI gate: perf budget check (Owner: DevEx, due: 2026-08-31)
    ```

5) Postmortem Creation
- Input: RCA draft + metrics and action items.
- Process: Generate a postmortem doc ready to publish to KB/Confluence; ensure blameless tone, clear actions, and links.
- Output: Postmortem markdown with sections.

    ```
    # Postmortem — INC-2451 (Blameless)
    Summary: ...
    Customer Impact: ...
    Detection & Response: ...
    Root Cause: ...
    Contributing Factors: ...
    Actions & Owners: ...
    Learnings & Follow-ups: ...
    References: [dashboards][PRs][deploys]
    ```

--------------------------------------------------------------------------------

## 2) Integrations & Configuration

- Observability
  - Logs: query endpoints (e.g., /_search, /loki/api/v1/query_range)
  - Metrics: Prometheus/Cloud metrics range queries for SLI windows
  - Traces: Jaeger/Tempo APIs for top error/latency spans
- On-call
  - PagerDuty/Opsgenie incidents, notes, schedules; post summaries and updates
- CMDB/KB
  - Service ownership map, runbooks, KB pages (read/write drafts)
- Change feeds
  - VCS tags/releases, CI job results, feature flag changes

Environment variables
- OBS_HOST, OBS_TOKEN
- ONCALL_HOST, ONCALL_TOKEN
- CMDB_HOST, CMDB_TOKEN
- KB_HOST, KB_TOKEN
- VCS_HOST, VCS_TOKEN
- MODEL_NAME, TOP_K (default: 5), TIME_WINDOW_MIN (default: 30)
- METRICS_STORE (path/URL)

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- MTTR (Mean Time To Restore)
  - Definition: time from first alert to restored service (alerts below threshold or manual declare).
  - Collection: correlate alert_fired_at with stabilized_at.

    ```json
    {"incident":"INC-2451","alert_fired_at":"2026-08-10T09:12:00Z","restored_at":"2026-08-10T09:37:00Z","mttr_minutes":25}
    ```

- Alert Fatigue Reduction
  - Definition: 1 - (deduped_pages / baseline_pages) over a fixed window; or % reduction in duplicate alerts merged.
  - Collection: count pages before vs after de-dup rollout.

    ```json
    {"window":"last_28d","baseline_pages":140,"deduped_pages":98,"reduction":0.30}
    ```

- Knowledge Reuse
  - Definition: % incidents resolved using existing runbooks/KB (linked as evidence) vs total incidents.
  - Collection: track incidents with kb_links[].length > 0.

    ```json
    {"period":"2026-08","incidents_total":42,"with_kb_links":25,"reuse_rate":0.60}
    ```

Reporting
- Weekly rollups by service/region; export CSV/Confluence; annotate with major changes.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Actions
  - Read-many, write-few; live commands are plans only unless explicitly approved.
  - Every plan and draft includes correlationId, sources, and redaction status.
- Security
  - No plaintext secrets; redact tokens and PII in logs and outputs.
  - Access tokens are least-privileged; rotate per policy; store only hashed references.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Incident triage
    ```
    "Correlate alerts/logs/traces in the last {TIME_WINDOW}. Output: summary, top hypotheses with confidence, and 1–3 next actions. Cite links for each hypothesis."
    ```

- Safe command plan
    ```
    "From the runbook and context, produce a Command Plan with: Pre-checks, Dry-run, Blast Radius, Actions, Rollback, Approvals. Never execute commands; redact secrets."
    ```

- Ticket de-dup/routing
    ```
    "Given alerts with fingerprints and CMDB ownership, select primary, list duplicates with reasons, and propose owner/on-call assignment."
    ```

- RCA/postmortem
    ```
    "Draft a blameless RCA with Timeline, 5 Whys, Contributing Factors, and Actions with owners/dates. Generate a publish-ready postmortem outline."
    ```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Phase 1: read-only incident triage and postmortem drafts; validate hypotheses quality.
- Phase 2: enable ticket merge/routing suggestions and command plans (approval-only).
- Phase 3: gated automation to populate KB/postmortems and attach artifacts to incidents.

--------------------------------------------------------------------------------

## 7) Accessibility & Quality

- Generated docs use clear headings, plain language, and avoid color-only cues.
- All links include descriptive text; timestamps are ISO8601 with timezone; evidence is concise and auditable.
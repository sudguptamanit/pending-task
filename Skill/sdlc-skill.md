What we’ll do:

Define “agent skill” and the core assumption.
Map practical agent skills to each SDLC phase with concrete, safe use cases.
Provide an implementation and governance checklist, plus a small diagram.
Assumption:

“Agent skill” = an AI agent (LLM-based) equipped with tools (APIs/SDKs) that can read/write to your SDLC systems (ALM, VCS, CI/CD, scanners), operating under policy with human-in-the-loop for risk-bearing actions.
How to use agent skills across SDLC

############

Planning & Requirements

Skills: stakeholder Q&A from existing docs; ambiguity/dedup detection in user stories; acceptance-criteria generator; compliance checklisting (e.g., security/privacy NFR prompts).
Integrations: read PRDs/Confluence, create/update Jira issues with traceability links; risk register draft.
Metrics: reduced requirement churn; acceptance-criteria coverage; traceability precision/recall.

###

Analysis

Skills: feasibility/risk assessment; quick tech spike outline; effort drivers and risks summarizer.
Integrations: repo scanning for prior art; dependency/license scan summaries.
Metrics: time-to-feasibility, forecast accuracy deltas.

###

Design

Skills: architecture assistant (C4 and ADR drafts); API contract first-draft; threat-model proposer (STRIDE) with mitigations.
Integrations: export diagrams/specs; open ADR PRs.
Metrics: review acceptance rate of ADRs; security findings caught pre-implementation.


####

Implementation

Skills: pair-programming with local repo context; secure-coding hints (OWASP); secrets/deps scanner triage; migration skeletons; refactor suggestions.
Integrations: PR descriptions, checklists, and suggested diffs; SonarQube/SAST finding explanations and fixes as PR comments.
Metrics: PR cycle time; % automated suggestions accepted; defect density downtrend.

####

Testing

Skills: unit/integration/e2e test generation from requirements and code paths; boundary/negative cases; mocking scaffolds; flaky test triage; perf test script drafts.
Integrations: map tests to requirements; coverage gap highlighting; CI failure root-cause summaries.
Metrics: coverage delta; escaped defects; flake rate reduction.

#####

CI/CD & Release

Skills: pipeline-as-code assistant (safe edits via PRs); change-impact analysis; release notes/changelog generation; canary/metrics analysis with rollback recommendation.
Integrations: YAML validation; artifact provenance checks; SBOM summarization.
Metrics: deployment frequency; change failure rate; MTTR for rollbacks.

####

Operations & Maintenance

Skills: incident copilot (log/trace triage, probable cause hypothesis); runbook retrieval and safe, auditable command composition; RCA draft; ticket routing and de-dup.
Integrations: observability (logs/metrics/traces), on-call, CMDB/KB; create postmortem docs.
Metrics: MTTR, alert fatigue reduction, knowledge reuse.

####
Implementation pattern (safe and effective)

Architecture

One orchestrator agent + tool adapters (ALM, VCS, CI, scanners, cloud, observability).
Roles: planner (breaks tasks), executor (calls tools), critic/verifier (policy/compliance checks).
Context: Retrieval-augmented inputs from repos/docs; tight scoping to least-privilege data.

####

Guardrails and policy

Data: no production secrets/PII to models; redact/role-based masking; log redaction.
Actions: read-many, write-few; destructive operations require human approval; all writes via PRs.
Security: OWASP practices; dependency/licensing checks; signed commits; audit trails.

###

Evaluation and monitoring

Golden tasks per phase (e.g., “generate boundary tests for X”); measure precision/recall, review-acceptance rate, PR cycle time, MTTR.
Offline evals before rollout; canary rollout to one team/repo; continuous feedback loop.
Observability: structured logs with correlation IDs; reason traces; cost/time budgets per task.

####
Minimal rollout plan

Week 1–2: Pick two low-risk skills (requirements ambiguity checker; unit-test generator). Define success metrics and approval rules.
Week 3–4: Integrate with ALM/VCS; run in read-only to baseline; then enable PR comment mode.
Week 5+: Expand to code review triage and release-notes generation; add security guardrails; formalize “human-in-the-loop” DoD updates.
Quarterly: Evaluate impact, retire low-value skills, harden high-ROI ones.
Diagram (high level placement) See agent-skill-sdlc.mmd.

Diagram: flowchart,LR


Agent Skills & Guardrails

Planner / Critic

Tool Adapters
(ALM, VCS, CI, Scanners, Obs)

Human-in-the-loop
(Approvals/Reviews)

Plan/Reqs
- Ambiguity check
- Traceability map

Design
- ADR draft
- Threat model

Build
- Pair coder
- Secure code scan

Test
- Unit/E2E gen
- Coverage gaps

Release
- Change impact
- Release notes

Operate
- Incident copilot
- RCA draft

Diagram source code
Key trade-offs

Benefit: Faster cycles, better quality/coverage, consistent compliance hygiene.
Risks: Hallucinations and unsafe actions—mitigated via narrow tools, approval gates, audits, and offline/online evaluations.
Adoption: Start small with high-signal, low-risk skills and expand based on measured ROI.
If you share your stack (ALM/VCS/CI, security tools), I can tailor a concrete integration plan and policies.
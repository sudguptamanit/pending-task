# Planning & Requirements Skill

Purpose
- Assist product/engineering in clarifying and validating requirements early by: answering stakeholder questions from existing docs, detecting ambiguity/duplicates in user stories, generating high-quality acceptance criteria, and producing compliance (security/privacy) checklists.
- Integrate with PRD repositories (e.g., Confluence), and ALM (e.g., Jira) to maintain end-to-end traceability and a draft risk register.

Scope
- Read-only on documents by default; write via comments/drafts/PRs with human approval for risk-bearing actions (issue creation, scope edits).

Key Capabilities
- Stakeholder Q&A from existing docs with citations and confidence.
- Ambiguity and de-duplication detection for user stories.
- Acceptance-criteria generation (Gherkin-first with boundary/negative paths).
- Compliance checklist prompts for security/privacy NFRs.
- Create/update Jira issues with traceability links; draft risk register.

Primary Integrations
- PRDs/Design/Notes: Confluence (REST), files (PDF/Docx/Markdown), shared drives (read).
- ALM: Jira Cloud/Data Center (issue read/write, links, comments).
- Output locations: Confluence page drafts/comments, Jira comments/fields, optional CSV/JSON exports.

Guardrails
- Least-privilege access; no production secrets/PII in prompts.
- All writes require explicit human approval; changes logged with correlation IDs.
- Transparent citations back to sources for every Q&A and suggested change.

--------------------------------------------------------------------------------

## 1) Workflows

1. Stakeholder Q&A (from existing docs)
   - Input: Natural-language question.
   - Process: Retrieve top-k passages from PRDs/Confluence; synthesize answer; include citations and confidence.
   - Output: Answer + links to source pages/anchors; unresolved tags if confidence < threshold.
   
       	```
       	Input: "What are SLAs for the payment API?"
       	Output:
       	- SLA: 99.9% monthly availability; p95 latency < 200ms.
       	- Source: https://confluence.example.com/display/PROJ/PRD#SLA
       	- Confidence: 0.82
       	- Gaps: No defined error budget policy; propose addition.
       	```

2. Ambiguity & Dedup Detection
   - Input: Set of user stories (e.g., Jira JQL or pasted list).
   - Process: 
     - Ambiguity: flag vague terms ("fast", "soon", "etc.", "TBD"), missing actors, unclear quantifiers, passive voice.
     - Dedup: semantic similarity clustering (embedding cosine) + title/label heuristics; propose merges/links.
   - Output: Per-story report with findings and suggested fixes.

       	```
       	Story: "As a user, I want fast checkout."
       	Ambiguity:
       	- "fast" ambiguous → propose: "p95 < 3s from cart to confirmation" (web), "p95 < 2s" (native).
       	- Actor vague → "registered shopper" vs "guest".
       	Dedup:
       	- Similar to PROJ-124, PROJ-171 (0.86, 0.81). Propose link "duplicates".
       	```

3. Acceptance-Criteria Generator (Gherkin-first)
   - Input: Single story (title, description, dependencies).
   - Process: Generate AC including happy path, boundary, error/negative cases, and non-functional checks tied to the story.
   - Output: Gherkin with tags that encode traceability.

       	```
       	Feature: Checkout Payment
       	@story:PROJ-321 @area:payments
       	Scenario: Successful card payment
       	  Given a registered shopper with items in the cart
       	  And a valid Visa card
       	  When the shopper submits payment
       	  Then the order is created
       	  And a confirmation email is sent within 60s
       	
       	Scenario: Card declined (insufficient funds)
       	  Given ...
       	  When ...
       	  Then a decline message is shown without leaking issuer details
       	
       	Scenario: Performance budget
       	  Given normal load
       	  When submitting payment
       	  Then p95 end-to-end latency <= 2s
       	```

4. Compliance Checklist (Security/Privacy NFR Prompts)
   - Input: Story/epic context + system classification (e.g., "handles PII", "financial").
   - Process: Map to relevant controls (authz/authn, data at rest/in transit, logging, retention, consent, DPIA triggers).
   - Output: Checklist with status, owner, due date, and links to controls.

       	```json
       	{
       	  "item": "PII data classification",
       	  "status": "Open",
       	  "owner": "SecurityChampion",
       	  "control": "Data classification policy v2.1",
       	  "evidence": null,
       	  "due": "2026-09-01",
       	  "links": ["https://confluence.../privacy#classification"]
       	}
       	```

5. Jira Integration (Create/Update + Traceability)
   - Create/update issue with AC in the description/comment; link to PRD page and related stories; set labels (e.g., "nfr:security", "trace:prdsync").
   - Add issuelinks ("duplicates", "relates to", "blocks") from dedup/impact analysis.
   - Post compliance checklist as a comment or attach JSON; add custom fields if available.

       	```
       	POST /rest/api/3/issue
       	body: {
       	  "fields": {
       	    "project": {"key": "PROJ"},
       	    "summary": "Checkout: Acceptance Criteria",
       	    "issuetype": {"name": "Task"},
       	    "description": "See AC below...\n\n<gherkin block>",
       	    "labels": ["trace:prdsync","nfr:security"]
       	  }
       	}
       	```

6. Risk Register Draft
   - Input: Epic/feature context and known dependencies.
   - Output: Risk entries with likelihood, impact, mitigation, owner, review date; exported to Confluence page draft or Jira "Risk" issue type.

       	```
       	| Risk | Likelihood | Impact | Mitigation | Owner | Review |
       	|------|------------|--------|------------|-------|--------|
       	| PSP gateway SLA changes | Medium | High | Add circuit breaker, retry + fallback | Eng Lead | 2026-09-15 |
       	```

--------------------------------------------------------------------------------

## 2) Data & Traceability Model

- Canonical IDs
  - PRD: PRD-<slug> (Confluence page ID/URL)
  - Requirement: REQ-<number> (custom field or page anchor)
  - Story: Jira key (e.g., PROJ-123)
  - AC: AC-<story>-<n> (tag in Gherkin)
  - Checklist item: CHK-<story>-<n>
  - Risk: RSK-<epic>-<n>

- Links
  - PRD → Story (sourceOf)
  - Story → AC (verifiableBy)
  - Story/Epic → Checklist (compliantWith)
  - Epic → Risk (hasRisk)

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- Requirement churn (lower is better)
  - Definition: (# stories/requirements changed or re-opened within a sprint) / (total active stories/requirements that sprint).
  - Collection: Compare Jira changelogs for fields: description, acceptance criteria, scope labels; windowed by sprint dates.

- Acceptance-criteria coverage (higher is better)
  - Definition: % of stories in "Ready for Dev" with ≥1 AC covering: happy, boundary, and negative paths.
  - Heuristic: Presence of Gherkin tags and at least three scenario types detected.

- Traceability precision/recall (higher is better)
  - Gold set: Curated sample of story↔PRD links (weekly).
  - Precision = correct_links_suggested / total_links_suggested.
  - Recall = correct_links_suggested / total_true_links.

- Reporting cadence: per sprint with sparkline trends; export CSV/Confluence macro.

       	```
       	metrics.sample = {
       	  "sprint": "2026-08",
       	  "churn": 0.18,
       	  "ac_coverage": 0.92,
       	  "trace_precision": 0.87,
       	  "trace_recall": 0.81
       	}
       	```

--------------------------------------------------------------------------------

## 4) Permissions & Configuration

- Confluence (read): read:confluence-content.summary, read:confluence-content.all
- Jira (read/write): read:jira-work, write:jira-work, manage:jira-project (optional for custom fields)
- Storage: vector index for embeddings; redact PII from chunks.
- Environment
  - CONF_HOST, CONF_TOKEN
  - JIRA_HOST, JIRA_TOKEN
  - INDEX_STORE (path/bucket), MODEL_NAME, TOP_K (default: 5)

--------------------------------------------------------------------------------

## 5) Non-functional & Security

- Input validation; safe error messages; timeouts and retries (idempotent only, exp backoff + jitter); circuit breakers on external calls.
- Audit: Structured logs with correlationId per action; human approval recorded on write actions.
- Accessibility: Generated AC and checklists formatted for screen readers; avoid color-only cues; clear headings.

--------------------------------------------------------------------------------

## 6) Prompts & Templates (summarized)

- Q&A synthesis
       	```
       	"You are a requirements assistant. Answer using only the provided sources. Cite each statement with [title#anchor]. If unsure, say so and list missing info."
       	```

- Ambiguity patterns
       	```
       	Vague terms: fast, quickly, etc., TBD, appropriate, robust, user-friendly, soon
       	Missing: actor, quantifier, error handling, performance target, privacy note
       	```

- Acceptance criteria (Gherkin)
       	```
       	Include: happy, boundary, negative, perf/security if applicable.
       	Tag: @story:{KEY} @area:{TAXONOMY}
       	```

- Compliance checklist seeds
       	```
       	AuthN/AuthZ, PII classification, data retention, encryption in transit/at rest, logging/PII redaction, consent, data subject rights, secrets handling, third-party processors, incident response hooks
       	```

--------------------------------------------------------------------------------

## 7) Example Jira Comment Payloads

       	```json
       	{
       	  "body": {
       	    "type": "doc",
       	    "version": 1,
       	    "content": [
       	      {"type":"paragraph","content":[{"type":"text","text":"Proposed Acceptance Criteria (Gherkin):"}]},
       	      {"type":"codeBlock","attrs":{"language":"gherkin"},"content":[{"type":"text","text":"Scenario: ..."}]}
       	    ]
       	  }
       	}
       	```

--------------------------------------------------------------------------------

## 8) Evaluation & Rollout

- Offline evals: curated Q&A and link-resolution sets; measure precision/recall pre-prod.
- Canary: run read-only for 2 sprints; enable comment-only writes; then gated issue updates.
- Documentation: Change log, known limitations, escalation path.

--------------------------------------------------------------------------------

## 9) Success Criteria (initial targets)

- Requirement churn ≤ 15% within 2 sprints.
- AC coverage ≥ 90% of "Ready for Dev" stories with all three scenario types.
- Traceability precision ≥ 85%, recall ≥ 80% on gold sets.

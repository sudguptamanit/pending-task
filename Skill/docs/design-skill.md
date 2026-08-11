# Design Skill

Purpose
- Accelerate high-quality design by drafting C4 diagrams and ADRs, producing first-pass API contracts (OpenAPI/AsyncAPI), and proposing a STRIDE-based threat model with actionable mitigations—exportable as diagrams/specs and submitted via ADR PRs.

Scope
- Default read across repos and design spaces; writes occur as comments/drafts/PRs with explicit human approval for any change to canonical docs.

Key Capabilities
- Architecture assistant: generate/iterate C4 (Context/Container/Component) sketches and ADR drafts.
- API contract first-draft: OpenAPI/AsyncAPI with schemas, errors, and examples.
- Threat-model proposer: STRIDE catalog of risks with prioritized mitigations and acceptance rationale.
- Integrations: export diagrams/specs (Mermaid → SVG/PNG/PDF, OpenAPI YAML/JSON), open ADR PRs with linked artifacts.

Guardrails
- Evidence-only references (repo/docs); list assumptions and unknowns.
- Least-privilege tokens; redact secrets/PII; all writes logged with correlation IDs.
- Human-in-the-loop: ADR creation and API changes always require review/approval.

--------------------------------------------------------------------------------

## 1) Workflows

1. Architecture Assistant (C4 + ADR Draft)
   - Input: Epic/story context, constraints/NFRs (latency, availability, data residency), target stack.
   - Process:
     - Summarize context and drivers; generate lightweight C4 views (Context, Container, optional Component).
     - Draft ADR capturing decision, alternatives, and security/privacy impact.
     - Attach citations to existing modules/prior art.
   - Output: C4 diagrams (Mermaid), ADR markdown draft, linkable artifacts.

       	```
       	ADR: 0001-use-event-driven-checkout
       	Status: Proposed
       	Date: 2026-08-10
       	Context:
       	- Peak TPS 300; decouple payment from order confirmation; avoid cart DB contention.
       	Decision:
       	- Adopt event-driven architecture with "OrderPlaced" topic; payment worker consumes and finalizes.
       	Consequences:
       	+ Scales horizontally; isolates failures
       	- Eventual consistency; requires idempotency keys
       	Alternatives:
       	- Synchronous REST payment (rejected: tight coupling)
       	Security/Privacy Impact:
       	- PII in events prohibited; encrypt tokens; strict authZ on consumers
       	References:
       	- repo://orders/service.go#L50-L120
       	```

       	```
       	C4 Notes (Context):
       	System: "Checkout Platform"
       	Primary User: "Shopper"
       	External Systems: "Payment PSP", "Email Service"
       	Responsibilities: "Place Orders", "Process Payment", "Notify"
       	```

       	```mermaid
       	flowchart TB
       	  actorA["Shopper"]
       	  subgraph S["Checkout Platform"]
       	    web["Web App"]
       	    api["API Gateway"]
       	    ordersvc["Order Service"]
       	    payworker["Payment Worker"]
       	  end
       	  psp["Payment PSP"]
       	  email["Email Service"]
       	  actorA --> web --> api --> ordersvc
       	  ordersvc -->|"emit 'OrderPlaced'"| payworker
       	  payworker -->|"Capture/Confirm"| psp
       	  ordersvc -->|"Send Confirmation"| email
       	```

2. API Contract First-Draft (OpenAPI/AsyncAPI)
   - Input: Story/use-cases, domain model, error taxonomy, NFRs.
   - Process: Propose resources/operations, request/response schemas, pagination, standard errors, examples.
   - Output: openapi.yaml (3.1), examples, error schema.

       	```yaml
       	openapi: 3.1.0
       	info:
       	  title: Checkout API
       	  version: 0.1.0
       	servers:
       	  - url: https://api.example.com
       	paths:
       	  /payments:
       	    post:
       	      summary: Create a payment for an order
       	      operationId: createPayment
       	      requestBody:
       	        required: true
       	        content:
       	          application/json:
       	            schema:
       	              $ref: "#/components/schemas/PaymentRequest"
       	      responses:
       	        "201":
       	          description: Payment created
       	          content:
       	            application/json:
       	              schema:
       	                $ref: "#/components/schemas/PaymentResponse"
       	        "400":
       	          description: Invalid request
       	          content:
       	            application/json:
       	              schema:
       	                $ref: "#/components/schemas/Error"
       	components:
       	  schemas:
       	    PaymentRequest:
       	      type: object
       	      required: [orderId, amount, currency, method, idempotencyKey]
       	      properties:
       	        orderId: {type: string, format: uuid}
       	        amount: {type: number, format: decimal, minimum: 0}
       	        currency: {type: string, pattern: "^[A-Z]{3}$"}
       	        method: {type: string, enum: [card, wallet]}
       	        idempotencyKey: {type: string, minLength: 8}
       	    PaymentResponse:
       	      type: object
       	      required: [paymentId, status]
       	      properties:
       	        paymentId: {type: string, format: uuid}
       	        status: {type: string, enum: [authorized, captured, declined, pending]}
       	        declineReason: {type: string, nullable: true}
       	    Error:
       	      type: object
       	      required: [code, message]
       	      properties:
       	        code: {type: string}
       	        message: {type: string}
       	        traceId: {type: string}
       	```

3. Threat Model (STRIDE) with Mitigations
   - Input: C4 views, API contract, data classification, trust boundaries.
   - Process: Enumerate STRIDE per asset + flow; rank by likelihood/impact; propose mitigations; mark accepted risks with rationale.
   - Output: JSON/markdown table + checklist of mitigations, owners, due dates.

       	```json
       	{
       	  "asset": "Payment Worker",
       	  "threats": [
       	    {"type":"Spoofing","vector":"Queue credentials","mitigation":"IRSA/Workload identity + scoped IAM","sev":"High","owner":"Platform"},
       	    {"type":"Tampering","vector":"Event payload","mitigation":"JWS signed events + schema validation","sev":"High","owner":"Backend"},
       	    {"type":"Repudiation","vector":"Missing audit","mitigation":"Structured logs + immutable store","sev":"Med","owner":"Backend"},
       	    {"type":"Information Disclosure","vector":"PII leakage","mitigation":"No PII in events; tokenization","sev":"High","owner":"Security"},
       	    {"type":"DoS","vector":"Burst traffic","mitigation":"Consumer concurrency caps + DLQ + backoff","sev":"Med","owner":"Platform"},
       	    {"type":"Elevation of Privilege","vector":"Overbroad role","mitigation":"Least-privilege IAM; code review gates","sev":"Med","owner":"Platform"}
       	  ]
       	}
       	```

4. Export & PR Flow
   - Export: Render Mermaid → SVG/PNG/PDF; persist openapi.yaml; bundle threat-model JSON/markdown.
   - Open ADR PR:
     - Create branch; add ADR + diagrams + api/ folder.
     - Open PR with checklist and links; request reviews (Architecture/Security).

       	```
       	PR Title: "ADR-0001: Event-driven Checkout + API v0.1 + Threat Model"
       	Description:
       	- ADR 0001 (Proposed)
       	- Diagrams: context.svg, container.svg
       	- API: api/openapi.yaml
       	- Threat Model: threats/checkout.json
       	Reviewers: @arch-team, @security-champions
       	Checklist:
       	- [ ] NFRs mapped
       	- [ ] Errors standardized
       	- [ ] STRIDE mitigations assigned
       	```

--------------------------------------------------------------------------------

## 2) Integrations

- VCS
  - Read: code/docs; Write (gated): branches, PRs, comments, ADR files.
- Docs/Design
  - Accept/emit Mermaid (.mmd), Markdown (.md), images (SVG/PNG/PDF).
- API Specs
  - OpenAPI/AsyncAPI YAML/JSON; lint via spectral (optional) with report artifact.
- Storage/Index
  - Vector index for prior art; KV/DB for metrics and run logs.

Configuration (env vars)
- VCS_HOST, VCS_TOKEN
- ADR_REPO (org/repo or URL), ADR_PATH (e.g., docs/adr)
- DIAGRAM_OUT (e.g., docs/diagrams), API_SPEC_PATH (e.g., api/openapi.yaml)
- MODEL_NAME, TOP_K (default: 5), METRICS_STORE

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- Review Acceptance Rate of ADRs
  - Definition: merged_or_approved_adrs / total_adrs_proposed in period.
  - Collection: scan PRs labeled "ADR" with states; attribute reasons for rejections when present.

       	```json
       	{"period":"2026-08","adrs_proposed":6,"adrs_accepted":5,"acceptance_rate":0.83}
       	```

- Security Findings Caught Pre-Implementation
  - Definition: count of unique security issues identified in design (STRIDE/docs review) before coding starts.
  - Collection: issues labeled "security" + "design" on ADR PRs; dedup by fingerprint.

       	```json
       	{"period":"2026-08","findings_pre_impl":7,"sev_breakdown":{"High":4,"Med":3,"Low":0}}
       	```

Reporting
- Per sprint rollup and trend; export CSV/Confluence if needed.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Tokens scoped read-most; PR/comment-only writes; merges require human approval.
- All artifacts hashed and logged with correlationId; citations required for claims.
- Security review mandatory for ADRs affecting trust boundaries, authZ, or data sensitivity.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Architecture assistant
       	```
       	"You are an architecture assistant. Produce C4 Context/Container summaries and an ADR draft. Cite prior art; list assumptions and unknowns. Avoid implementation detail beyond interfaces and responsibilities."
       	```

- API contract drafter
       	```
       	"Draft OpenAPI 3.1 with resources, verbs, request/response schemas, pagination, standard errors, and examples. Enforce idempotency and consistent error envelope."
       	```

- STRIDE threat modeller
       	```
       	"Enumerate STRIDE threats per asset/flow. Rank by sev (High/Med/Low) with mitigations, owner, and due date. Mark accepted risks with rationale."
       	```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Dry runs on past designs; compare against gold ADRs and security reviews.
- Enable comment-only PRs for 2 sprints; then gated ADR PR creation.
- Success thresholds: ADR acceptance ≥ 80%; ≥ 5 pre-implementation security findings per major design where applicable.

--------------------------------------------------------------------------------

## 7) Accessibility & Quality

- Generated docs use clear headings, alt-text for images, and avoid color-only cues.
- Diagrams include text labels and are exported with sufficient contrast and font size.
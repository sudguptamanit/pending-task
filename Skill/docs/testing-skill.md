# Testing Skill

Purpose
- Increase quality and reduce risk by generating unit/integration/e2e tests from requirements and code paths, covering boundary/negative cases, scaffolding mocks, triaging flaky tests, and drafting performance test scripts. Integrates with traceability (requirements↔tests), highlights coverage gaps, and summarizes CI failure root causes.

Scope
- Read repo, requirements, coverage, and CI logs; write suggested tests as diffs or PR comments. Any commits/merges require explicit human approval.

Key Capabilities
- Test generation (unit/integration/e2e) from requirements and code path analysis.
- Boundary and negative case expansion with data-driven matrices.
- Mocking scaffolds for external calls, time, randomness, and I/O.
- Flaky test triage (signal classification, quarantine proposal, stabilization steps).
- Performance test script drafts (k6/Locust/JMeter) with budgets and assertions.
- Map tests to requirements (traceability) and highlight coverage gaps.
- CI failure root-cause summaries with minimal, actionable fixes.

Primary Integrations
- VCS/PR: draft diffs, comments, and test files.
- ALM: link tests to requirements/stories; maintain trace tags.
- Coverage: ingest Istanbul/JaCoCo/Cobertura reports to find gaps.
- CI: parse logs/artifacts (JUnit XML/SARIF/console) for RCA summaries.

Guardrails
- Evidence-only assertions with file/line citations; uncertain items marked explicitly.
- No network calls in generated tests unless explicitly requested; prefer mocks/fakes.
- Least-privilege tokens; redact secrets/PII; structured audit logs with correlation IDs.
- Human-in-the-loop for any write beyond comments/drafts.

--------------------------------------------------------------------------------

## 1) Workflows

1) Unit Test Generation (from code paths + requirements)
- Input: Target function/class, language/framework, linked requirement(s).
- Process: Build minimal white-box path set; derive happy/boundary/negative cases; scaffold mocks for I/O; tag tests with requirement IDs.
- Output: Test file diff + rationale; mapping JSON for traceability.

    ```diff
    diff --git a/src/math/divide.js b/src/math/divide.js
    new file mode 100644
    --- /dev/null
    +++ b/src/math/divide.js
    @@ -0,0 +1,10 @@
    + export function divide(a, b) {
    +   if (b === 0) throw new Error("DivisionByZero");
    +   if (typeof a !== "number" || typeof b !== "number") throw new TypeError("NaN");
    +   return a / b;
    + }
    ```

    ```javascript
    // Jest unit tests with requirement tags
    // @story:PROJ-321 @req:REQ-9
    describe("divide", () => {
      test("happy: divides positive numbers", () => {
        expect(divide(6, 3)).toBe(2);
      });
      test("boundary: division by zero throws", () => {
        expect(() => divide(1, 0)).toThrow("DivisionByZero");
      });
      test("negative: non-number inputs throw TypeError", () => {
        expect(() => divide("6", 3)).toThrow(TypeError);
      });
    });
    ```

    ```json
    {
      "trace": [
        {"test":"src/math/divide.test.js#happy","req":"REQ-9"},
        {"test":"src/math/divide.test.js#boundary","req":"REQ-9"},
        {"test":"src/math/divide.test.js#negative","req":"REQ-9"}
      ]
    }
    ```

2) Integration Test Scaffold (service + DB/API)
- Input: Target module, contracts, dependencies.
- Process: Stand up local fake or containerized dependency; inject config; seed data; verify end-to-end slice.
- Output: Integration test + docker-compose or testcontainer hints.

    ```python
    # pytest example with Testcontainers (Postgres)
    import os
    import psycopg2
    import pytest
    from testcontainers.postgres import PostgresContainer
    from app.repo import create_user, get_user
    
    @pytest.fixture(scope="module")
    def pg():
        with PostgresContainer("postgres:15") as pg:
            os.environ["DB_URL"] = pg.get_connection_url()
            yield
    
    def test_create_and_get_user(pg):
        uid = create_user(email="user@example.com")
        u = get_user(uid)
        assert u.email == "user@example.com"
    ```

3) E2E Test Draft (accessibility-first selectors)
- Input: Critical user flow, routes, ARIA roles.
- Process: Generate Playwright/Cypress draft using role/name selectors; include perf budget and a11y check.
- Output: E2E script + tags for traceability.

    ```javascript
    // Playwright example with role-based selectors
    test("@story:PROJ-450 checkout success", async ({ page }) => {
      await page.goto("/cart");
      await page.getByRole("button", { name: "Checkout" }).click();
      await page.getByLabel("Card Number").fill("4111111111111111");
      await page.getByRole("button", { name: "Pay" }).click();
      await expect(page.getByRole("heading", { name: "Order Confirmed" })).toBeVisible();
    });
    ```

4) Mocking Scaffolds
- Input: External dependency signatures.
- Process: Propose fakes/mocks/stubs with deterministic outputs and time/random control.

    ```javascript
    // Jest HTTP client mock
    jest.mock("../lib/http", () => ({
      get: jest.fn().mockResolvedValue({ status: 200, data: { id: "123" } }),
    }));
    ```

    ```python
    # pytest monkeypatch for time
    def test_token_expiry(monkeypatch):
        monkeypatch.setattr("app.time.time", lambda: 1_700_000_000)
        assert is_valid(token="...") is True
    ```

5) Flaky Test Triage
- Input: CI runs history, test outcomes, durations, suite order, resource metrics.
- Process: Classify cause (order-dependence, async race, timeouts, infra); propose fix and optional quarantine tag with expiry; generate RCA comment.

    ```json
    {
      "test": "Checkout > creates order",
      "flaky_score": 0.82,
      "likely_causes": ["async-race","clock-skew"],
      "evidence": {"failures": 3, "passes": 7, "p95_ms": 4800, "timeout_ms": 5000},
      "actions": ["increase-timeout-to-8000","await-network-idle","fix clock dependency"],
      "quarantine": {"recommended": true, "tag": "@flaky", "expires": "2026-09-15"}
    }
    ```

6) Performance Test Script Draft
- Input: Endpoint(s), load model, SLOs.
- Process: Draft k6/Locust with thresholds for latency/error rate; parameterize base URL and VUs; add ramp pattern.

    ```javascript
    // k6 example
    import http from "k6/http";
    import { check, sleep } from "k6";
    export const options = {
      vus: 20, duration: "1m",
      thresholds: {"http_req_duration{scenario:createPayment}":["p(95)<200"]},
    };
    export default function () {
      const res = http.post(`${__ENV.BASE_URL}/payments`, JSON.stringify({amount: 10, currency: "USD"}), {headers: {"Content-Type": "application/json"}});
      check(res, {"status is 201": (r) => r.status === 201});
      sleep(1);
    }
    ```

7) Coverage Gap Highlighting
- Input: Coverage reports (Istanbul/JaCoCo).
- Process: Identify uncovered branches/lines for high-risk modules; propose focused tests.

    ```json
    {
      "file": "src/auth/validator.js",
      "statements": {"pct": 67},
      "branches": {"pct": 40, "uncovered": ["L42:false", "L57:error"]},
      "suggestions": ["Add negative test for invalid email domain", "Trigger error path for null password"]
    }
    ```

8) CI Failure Root-Cause Summary (RCA)
- Input: JUnit XML/console logs/artifacts.
- Process: Cluster failures; extract first failing assertion/stack; map to likely cause and fix; attach links.

    ```
    Failure Group: TimeoutError in e2e checkout (7 runs)
    Likely Cause: network idle not awaited; page transitions in SPA
    Minimal Fix: await page.waitForLoadState('networkidle'); increase timeout to 10s
    Links: job/123#L240, job/127#L255
    ```

--------------------------------------------------------------------------------

## 2) Integrations & Configuration

- VCS/PR: create draft tests, post comments, suggested diffs.
- ALM: update requirement↔test links (labels or custom fields).
- Coverage: ingest JSON/XML (Istanbul/JaCoCo/Cobertura).
- CI: read logs/artifacts; post RCA comment and optional quarantine label.

Environment variables
- VCS_HOST, VCS_TOKEN
- ALM_HOST, ALM_TOKEN
- COVERAGE_PATHS (glob), CI_API, CI_TOKEN
- MODEL_NAME, TOP_K (default: 5), DIFF_MAX_LINES (default: 80)
- METRICS_STORE (path/URL)

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- Coverage Delta
  - Definition: coverage_after - coverage_before for statements/branches/funcs/lines.
  - Collection: compare last two main-branch reports; attribute to PRs by touched files.

    ```json
    {"period":"2026-08","statements_delta":+6.2,"branches_delta":+8.5}
    ```

- Escaped Defects
  - Definition: defects discovered post-release that tests should have caught (mapped to missing/insufficient tests).
  - Collection: tag production incidents/bugs with affected component and root cause; back-map to test gaps.

    ```json
    {"release":"2026.08.2","escaped_defects":3,"by_area":{"auth":1,"checkout":2}}
    ```

- Flake Rate Reduction
  - Definition: 1 - (current flaky_failures / baseline flaky_failures) over a fixed window.
  - Collection: deduplicate by test id; exclude infra outages.

    ```json
    {"window":"last_14d","baseline":52,"current":21,"reduction":0.60}
    ```

Reporting
- Per sprint trends with pointers to top modules improving/declining; export CSV/Confluence.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Comment/PR-body writes allowed; commits/merges gated.
- No secret values echoed; redact sensitive tokens/URLs in logs.
- All actions logged with correlationId; each suggestion cites file/line.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Test generator
    ```
    "Generate unit/integration/e2e tests from code paths and requirements. Include happy, boundary, and negative cases. Use framework-idiomatic patterns and deterministic mocks. Tag with @story and @req when provided."
    ```

- Flaky triage
    ```
    "Given N recent CI runs with outcomes/durations/logs, classify flake cause, evidence, and minimal fix. Output JSON with actions and optional quarantine tag with expiry."
    ```

- Perf script
    ```
    "Draft a k6/Locust test for the given endpoints and SLOs with thresholds and a simple ramp. Parameterize base URL and concurrency."
    ```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Phase 1: read-only; local preview of tests and RCAs.
- Phase 2: enable PR comments and suggested test diffs; gated quarantine label application.
- Phase 3: gated autofix patches (e.g., test timeout increases) that pass CI.

--------------------------------------------------------------------------------

## 7) Accessibility & Quality

- E2E selectors prefer roles/names over CSS; include basic a11y checks.
- Tests are fast, isolated, hermetic; no flakiness from time/network randomness.
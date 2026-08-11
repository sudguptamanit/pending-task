# CI/CD & Release Skill

Purpose
- Safely improve and automate delivery by assisting with pipeline-as-code (via PRs), change-impact analysis, release notes/changelog generation, and canary/metrics analysis with rollback recommendations. Integrates YAML validation, artifact provenance checks, and SBOM summarization to strengthen supply chain security.

Scope
- Read CI configs, build logs, artifacts, SBOMs, and release history; write only draft diffs and PR/comments. All changes to pipelines/releases are human-approved.

Key Capabilities
- Pipeline-as-code assistant: validate/lint YAML, propose minimal, reviewable improvements via PRs.
- Change-impact analysis: map diffs → services/tests/pipelines; estimate risk; propose gates.
- Release notes/changelog: conventional-commit parsing; grouped notes; breaking-change callouts.
- Canary analysis: compare baseline vs canary metrics; recommend continue/hold/rollback with evidence.
- Supply chain: artifact provenance (attestations/signatures) checks; SBOM summarization and risk flags.

Primary Integrations
- VCS/PR: GitHub/GitLab/Bitbucket (draft diffs, comments).
- CI runners: GitHub Actions/GitLab CI/Circle (YAML validation, dry-runs when supported).
- Artifact registry + attestations: OCI registries, Sigstore/Cosign (verify signatures/provenance).
- SBOM: CycloneDX/SPDX ingest; summarize vulns/licenses and provenance gaps.

Guardrails
- PR-only changes for pipelines; no direct pushes. All risky actions (rollback triggers, prod config edits) require explicit human approval.
- Least-privilege tokens; redact secrets; no echoing of tokens/keys.
- Evidence-first outputs with file/line/job links and metric snapshots; uncertainty explicitly marked.

--------------------------------------------------------------------------------

## 1) Workflows

1) Pipeline-as-Code Assistant (safe edits via PRs)
- Input: Target pipeline YAML(s), CI vendor, desired improvements (e.g., caching, concurrency, permissions).
- Process: Lint/validate, simulate where possible; generate minimal diffs (≤80 lines) and PR description/checklist.
- Output: Suggested diff + rationale; YAML validation report.

    ```diff
    diff --git a/.github/workflows/ci.yml b/.github/workflows/ci.yml
    index 12ab34..56cd78 100644
    --- a/.github/workflows/ci.yml
    +++ b/.github/workflows/ci.yml
    @@ -1,6 +1,10 @@
     name: CI
     on: [push, pull_request]
     jobs:
    -  build:
    +  build:
    +    permissions:
    +      contents: read
    +      id-token: write  # needed for OIDC provenance upload
        runs-on: ubuntu-latest
        steps:
    -     - uses: actions/checkout@v3
    +     - uses: actions/checkout@v4
    +     - uses: actions/setup-node@v4
    +       with: { node-version: "20", cache: "npm" }
           - run: npm ci
           - run: npm test -- --ci
    +     - name: "Verify provenance"
    +       run: cosign verify-blob --key $COSIGN_PUB --signature dist.sig dist.tgz
    ```

    ```
    PR Title: "ci: least-privilege perms + node cache + provenance verify"
    Summary:
    - Adds minimal permissions, Node cache, and provenance verification.
    - Keeps diff small and vendor-idiomatic; validated with YAML linter.
    Checklist:
    - [ ] YAML validated
    - [ ] Secrets not exposed
    - [ ] Dry-run/plan succeeded (if supported)
    - [ ] Owners reviewed (CI, Security)
    ```

2) Change-Impact Analysis
- Input: Commit range/PR, service map (path→service), test matrix, deployment topology.
- Process: Map changed files → affected services/jobs; select tests; compute risk (surface: prod code, migrations, infra changes).
- Output: JSON summary + recommended gates (extra tests, canary duration).

    ```json
    {
      "pr": 1342,
      "services": ["checkout-api","payments-worker"],
      "pipelines": ["ci.yml#build","deploy.yml#staging"],
      "selected_tests": ["unit:payments","e2e:checkout"],
      "risk": {"level":"High","reasons":["db-migration","authz change"]},
      "recommended_gates": ["require e2e on staging","extend canary to 30m","error-budget check"]
    }
    ```

3) Release Notes/Changelog Generation
- Input: Tag range, commit messages (conventional commits), merged PRs, labels.
- Process: Group by type (feat/fix/perf/docs), breaking changes first; include links and owners.
- Output: Markdown release notes + CHANGELOG entry.

    ```
    ## 2026.08.3 (2026-08-10)
    ### Breaking
    - feat!: Payments require idempotencyKey (PR #1321)
    ### Features
    - feat: Add canary verification step with OIDC provenance (PR #1330)
    ### Fixes
    - fix(checkout): Prevent double-capture on retry (PR #1328)
    ### Ops
    - ci: cache Node 20 + least-priv perms (PR #1319)
    ```

4) Canary/Metrics Analysis with Rollback Recommendation
- Input: Metrics (latency/error rate/saturation), SLOs/budgets, baseline window, canary window, feature flags.
- Process: Compare baseline vs canary; run statistical checks (non-parametric where possible); produce decision and rationale.
- Output: Decision (proceed/hold/rollback) + evidence and minimal next action.

    ```json
    {
      "service": "checkout-api",
      "baseline": {"p95_ms": 180, "error_rate": 0.3},
      "canary":   {"p95_ms": 260, "error_rate": 1.4},
      "thresholds": {"p95_ms": 220, "error_rate": 0.8},
      "decision": "rollback",
      "reason": "p95 +44% over SLO; errors +1.1pp vs threshold",
      "actions": ["flip feature flag OFF","roll back to 2026.08.2","open incident low-sev with RCA template"]
    }
    ```

5) Artifact Provenance Checks & SBOM Summarization
- Input: Artifact digest, attestation/signatures, SBOM (CycloneDX/SPDX), policy.
- Process: Verify signatures/attestations; validate builder/subject; parse SBOM for critical vulns and license issues.
- Output: Pass/Fail with evidence; SBOM digest and high-risk findings.

    ```
    Provenance:
    - subject: ghcr.io/acme/checkout@sha256:abcd... (match)
    - builder: actions@github.com (trusted)
    - signature: cosign ok (key: cosign.pub v3)
    SBOM Summary:
    - deps: 154; critical vulns: 0; high: 2 (lodash 4.17.19→4.17.21, openssl 1.1.1u→1.1.1w)
    - licenses: MIT (majority), GPL-3.0 transitive (flag for review)
    Decision: proceed with warning; schedule dep upgrades within 1 sprint.
    ```

--------------------------------------------------------------------------------

## 2) Integrations & Configuration

- VCS/PR: create suggested diffs and PR comments; label PRs (ci-change, release-notes).
- CI: YAML validation (built-in lints or external), dry-run/plan when available.
- Registry/Provenance: Cosign/Sigstore verification; policy checks.
- SBOM: ingest CycloneDX/SPDX JSON; summarize risks.

Environment variables
- VCS_HOST, VCS_TOKEN
- CI_API, CI_TOKEN, CI_VENDOR (github|gitlab|circle)
- REGISTRY_URL, COSIGN_PUB (public key path or keyless policy)
- SBOM_PATHS (glob), POLICY_PATH (e.g., json/yaml)
- MODEL_NAME, DIFF_MAX_LINES (default: 80), TOP_K (default: 5)
- METRICS_STORE (path/URL)

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- Deployment Frequency
  - Definition: count of prod deployments per day/week (or per service).
  - Collection: read deployment events/tags to prod; attribute by service.

    ```json
    {"window":"2026-W33","service":"checkout-api","deployments":18}
    ```

- Change Failure Rate (CFR)
  - Definition: failures requiring hotfix/rollback / total deployments in period.
  - Collection: detect rollbacks/hotfix tags/incidents linked to releases.

    ```json
    {"window":"2026-W33","deployments":18,"failures":2,"cfr":0.111}
    ```

- MTTR for Rollbacks
  - Definition: time from detection (first alert) to restored service (rollback completed).
  - Collection: correlate alert timestamps with rollout/rollback job completion.

    ```json
    {"incident":"INC-2041","detected":"2026-08-10T09:12:00Z","restored":"2026-08-10T09:37:00Z","mttr_minutes":25}
    ```

Reporting
- Per service and aggregate trends; export CSV/Confluence; annotate with major changes.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Only PR/comment writes; merges/rollbacks gated by approvers (Release/On-call/Security as applicable).
- No plaintext secrets in logs or artifacts; attestations and SBOMs stored in approved locations.
- Every recommendation cites sources (job links, artifact digests, metric snapshots) with correlationId.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Pipeline PR patch
    ```
    "Given this CI YAML and vendor, lint and propose a minimal diff that adds least-privilege permissions, caching, and provenance checks. Keep vendor-idiomatic syntax and ≤80 changed lines. Output unified diff + PR body with checklist."
    ```

- Change-impact
    ```
    "Map the diff to services/pipelines/tests and compute a risk score. Output JSON with services[], pipelines[], selected_tests[], risk{level,reasons[]}, and recommended_gates[]."
    ```

- Release notes
    ```
    "From commits and PRs between tags, generate Markdown notes grouped by type, with breaking changes first and links per PR. Keep to 200–300 words."
    ```

- Canary verdict
    ```
    "Compare baseline vs canary metrics against thresholds. Output decision (proceed|hold|rollback) with brief reason and 1–3 next actions."
    ```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Phase 1: read-only; YAML lint and canary advice in preview.
- Phase 2: enable PR comments and suggested diffs; gated application of labels and release notes commits.
- Phase 3: gated auto-create rollback PRs/runbooks when decision=rollback and approvers sign off.

--------------------------------------------------------------------------------

## 7) Accessibility & Quality

- Generated PR/release text uses clear headings, concise bullets, and avoids color-only cues.
- Evidence is compact and link-rich for quick human review; numbers include units and time windows.
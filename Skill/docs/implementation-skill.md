# Implementation Skill

Purpose
- Accelerate safe, high-quality coding by pairing in-repo with developers, surfacing secure-coding guidance (OWASP), triaging secrets/dependency findings, scaffolding migrations, and proposing small, reviewable refactors. Integrates with PR workflows (descriptions, checklists, suggested diffs) and explains SonarQube/SAST findings with actionable fixes as PR comments.

Scope
- Read repo context locally (files, history, CI logs, scanner outputs). Writes occur as draft diffs and PR comments; commits/merges require explicit human approval.

Key Capabilities
- Pair-programming with local repo context (neighborhood-aware suggestions; test hints).
- Secure-coding hints (OWASP/ASVS, CWE) with concrete code examples.
- Secrets and dependency scanner triage with prioritized fixes.
- Migration skeletons (DB/data/app) with rollback patterns.
- Refactor suggestions (small, verifiable diffs) plus before/after tests.

Primary Integrations
- VCS/PR: draft PR descriptions, checklists, and suggested diffs/patches.
- Code Quality/Security: SonarQube + SAST tools (e.g., CodeQL, Semgrep) explanations and proposed fixes as PR comments.
- Scanners: secrets (gitleaks/trufflehog), SCA/SBOM (e.g., OWASP DC, Snyk, osv.dev).

Guardrails
- Human-in-the-loop for any write: only draft diffs and PR comments are auto-emitted.
- Secret hygiene: never reprint discovered secrets; propose rotation/removal steps; history rewrite guidance remains gated.
- Least-privilege tokens; redact PII; structured audit logs with correlation IDs for all actions.
- Small-change bias: prefer ≤50-line diffs with tests; larger changes split by concern.

--------------------------------------------------------------------------------

## 1) Workflows

1. Pair-Programming with Local Repo Context
- Input: Current file, related modules (imports/references), failing tests/lints.
- Process: Build a small context window (AST + dependency map), propose focused change with rationale and tests.
- Output: Suggested diff + note of impacted tests/paths.

    ```diff
    diff --git a/src/auth/validator.js b/src/auth/validator.js
    index 1a2b3c..4d5e6f 100644
    --- a/src/auth/validator.js
    +++ b/src/auth/validator.js
    @@ -12,6 +12,12 @@ export function validateLogin(input) {
       if (!input) throw new Error("Invalid input");
    -  const { username, password } = input;
    +  const { username, password } = input;
    +  // Enforce normalization to prevent auth bypass via Unicode confusables
    +  const normUser = username?.normalize?.("NFKC");
    +  if (!normUser || typeof password !== "string" || password.length < 12) {
    +    throw new Error("Invalid credentials format");
    +  }
    -  return /^[\w.-]{3,}$/.test(username) && password.length > 0;
    +  return /^[\w.-]{3,}$/.test(normUser) && password.length >= 12;
    }
    ```

2. Secure-Coding Hints (OWASP-aligned)
- Input: Changed lines + language + framework.
- Process: Map patterns to OWASP ASVS/CWE; suggest safer APIs/validation/encoding; include minimal example and risk explanation.
- Output: PR comment with rule, impact, fix.

    ```
    Comment: "Validate and encode outputs to prevent XSS"
    Standard: OWASP ASVS V4.0.3 5.3.2, CWE-79
    Risk: Untrusted input rendered without encoding
    Fix (React):
    // Use dangerouslySetInnerHTML only with sanitizer; prefer text content
    ```

3. Secrets & Dependency Scanner Triage
- Input: gitleaks/trufflehog JSON; SCA/SBOM results.
- Process: Classify severity, ownership, and fix path (rotate secret, purge file, upgrade dep).
- Output: Action list + optional patch to externalize config.

    ```json
    {
      "secrets": [
        {"file":"config/dev.env","line":14,"type":"AWS","action":"Rotate + remove; use env var","owner":"Platform","sev":"High"}
      ],
      "deps": [
        {"pkg":"lodash","current":"4.17.19","fix":"4.17.21","sev":"Med","owner":"Frontend"}
      ]
    }
    ```

    ```diff
    diff --git a/config/dev.env b/config/dev.env
    index 7b8c9d..000000 100644
    --- a/config/dev.env
    +++ /dev/null
    @@ -1,3 +0,0 @@
    - AWS_SECRET_ACCESS_KEY=AKIA...REDACTED
    - DB_PASSWORD=supersecret
    - JWT_SECRET=rawplaintext
    ```

4. Migration Skeletons (DB/Data/App)
- Input: Desired schema/data change; target migration tool.
- Process: Generate idempotent up/down with safety checks; include lock/timeout guidance and rollback notes.
- Output: Skeleton migration with verification snippet.

    ```
    -- 20260810_add_customer_index.up.sql
    BEGIN;
    CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_customer_email ON customer(email);
    COMMIT;
    
    -- 20260810_add_customer_index.down.sql
    BEGIN;
    DROP INDEX IF EXISTS idx_customer_email;
    COMMIT;
    
    Verification:
    EXPLAIN ANALYZE SELECT * FROM customer WHERE email='user@example.com';
    ```

5. Refactor Suggestions (Small, Test-backed)
- Input: Target file(s) and smell type (duplication, long function, mixed concerns).
- Process: Suggest extraction/rename with behavior-preserving diff + unit tests update.
- Output: Diff + rationale + test additions.

    ```diff
    diff --git a/src/util/date.ts b/src/util/date.ts
    --- a/src/util/date.ts
    +++ b/src/util/date.ts
    @@ -1,8 +1,13 @@
    -export function toIso(d: Date) {
    -  return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
    -}
    +export function toIso(d: Date) {
    +  // Zero-pad month/day; use UTC to avoid tz skew
    +  const y = d.getUTCFullYear();
    +  const m = String(d.getUTCMonth()+1).padStart(2,"0");
    +  const day = String(d.getUTCDate()).padStart(2,"0");
    +  return `${y}-${m}-${day}`;
    +}
    ```

6. PR Description, Checklist, and Suggested Diffs
- Output: Auto-generated PR body with risks, test plan, migration check, and security gates.

    ```
    Title: "feat(auth): normalize usernames + enforce strong passwords"
    Summary:
    - Normalize to NFKC; min password length 12
    - Adds unit tests for edge cases (unicode confusables)
    Impact: Low risk; auth-only; no DB changes
    Test Plan:
    - yarn test auth: 18/18 passing
    Checklist:
    - [ ] SAST clean or waivers justified
    - [ ] Secrets scan clean
    - [ ] Added/updated tests
    - [ ] Docs/CHANGELOG updated if user-facing
    ```

7. SonarQube/SAST Findings → Explanations + Fixes
- Input: SonarQube/SARIF findings with locations.
- Output: Per-finding PR comment: cause, exploitability, minimal fix; batch autofix patch when safe.

    ```
    Finding: "Use of eval" (Major) in src/utils/templater.js:44
    Why: CWE-95; arbitrary code execution risk
    Fix:
    // Replace eval with safe template interpolation
    const output = template.replace(/\{\{(\w+)\}\}/g, (_, k) => map[k] ?? "");
    Refs: SonarQ profile JS-S1481
    ```

--------------------------------------------------------------------------------

## 2) Integrations & Configuration

- VCS/PR: GitHub/GitLab/Bitbucket (repos, branches, PRs, comments, checks).
- SonarQube/SAST: read issues, post comments; optional autofix suggestions.
- Secrets/SCA: ingest JSON (gitleaks/trufflehog, osv.dev/Snyk/OWASP-DC).
- Local tools: linter/test runners for quick verification.

Environment variables
- VCS_HOST, VCS_TOKEN
- SONAR_HOST, SONAR_TOKEN
- SCAN_BUCKET or SCAN_API, SCAN_TOKEN
- MODEL_NAME, CONTEXT_MAX_FILES (default: 20), DIFF_MAX_LINES (default: 50)
- METRICS_STORE (path/URL)

--------------------------------------------------------------------------------

## 3) Metrics (definitions and collection)

- PR Cycle Time
  - Definition: time from PR opened to merged (or first review to merge, configurable).
  - Collection: PR timestamps; exclude weekends/holidays optional.

    ```json
    {"pr":"proj#1234","opened":"2026-08-10T09:00:00Z","merged":"2026-08-10T15:30:00Z","cycle_time_h":6.5}
    ```

- % Automated Suggestions Accepted
  - Definition: (agent-suggested lines merged) / (agent-suggested lines proposed).
  - Collection: tag suggestions; compare merged diff hunks.

    ```json
    {"period":"2026-08","suggested":3200,"merged":2240,"accept_rate":0.70}
    ```

- Defect Density Downtrend
  - Definition: defects per KLOC over time (SAST true positives + post-merge bugs).
  - Collection: dedup SAST with fingerprints; count verified bugs; normalize by KLOC change.

    ```json
    {"period":"2026-08","kloc":52.3,"defects":21,"defects_per_kloc":0.40,"trend":"down"}
    ```

Reporting
- Sprint-level rollups with sparklines; CSV/Confluence exports.

--------------------------------------------------------------------------------

## 4) Permissions & Policy

- Comment/PR-body writes allowed; commits/merges gated.
- No plaintext secrets echoed; remediation guidance avoids exposing tokens.
- All actions logged with correlationId; citations to files/lines for each suggestion.

--------------------------------------------------------------------------------

## 5) Prompts & Templates (summarized)

- Secure-coding advisor
    ```
    "You are a secure-coding assistant. Map issues to OWASP ASVS and CWE. Provide a 1–2 line risk summary and the minimal fix using framework-idiomatic APIs. If unsure, say so."
    ```

- Refactor (small, safe)
    ```
    "Propose a ≤50-line diff that preserves behavior and improves readability/testability. Include rationale and any test updates."
    ```

- PR description/checklist
    ```
    Fields: Title, Summary, Impact, Risk, Test Plan, Checklist (SAST, secrets, tests, docs), Migration Note (if any).
    ```

- Scanner triage
    ```
    "Classify findings by severity/owner. Output JSON with file/line, type, action, owner, sev. Do not echo secret values."
    ```

--------------------------------------------------------------------------------

## 6) Evaluation & Rollout

- Phase 1 (2 sprints): read-only; suggest diffs in local preview; no PR writes.
- Phase 2: enable PR comments and PR-body drafts; no code pushes.
- Phase 3: gated branch pushes for autofix patches that pass tests/linters.
- Targets: cycle time -20% within 2–3 sprints; ≥60% suggestion acceptance; sustained defect density downtrend across 2 releases.

--------------------------------------------------------------------------------

## 7) Accessibility & Quality

- Generated PR content uses clear headings, plain language, and avoids color-only cues.
- Code examples include comments for intent; diffs are small and localized for easy review.
# System Bars Fork Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Publish an installable, independently signed BrightSports APK from the `system-bars` branch of `MHolmes91/BrightSports`.

**Architecture:** Compose safe-drawing insets protect the ordinary activity UI while an API 35 instrumentation test pins both system-bar boundaries. The existing upstream release workflow is extended to `system-bars` and uses a fork-owned release keystore stored in GitHub Actions secrets.

**Tech Stack:** Kotlin, Jetpack Compose, Android API 35, Gradle, GitHub Actions, GitHub CLI, Java `keytool`

**Spec:** `docs/superpowers/specs/2026-10-14-system-bars-fork-design.md`

## Global Constraints

- Keep package ID `com.gios.lightsports` so fork releases update one another.
- The upstream app must be uninstalled once because its private signing key is unavailable.
- Never commit the fork keystore or its password.
- Leave LightOS score-alert overlay behavior unchanged.
- Mirror the upstream release workflow and pinned action versions.

## Review Focus

- API 35 status-bar inset: content starts at or below the top system inset.
- Gesture navigation inset: content ends at or above the bottom system inset.
- Signing continuity: the generated APK certificate equals `signing-fingerprint.txt`.
- Missing optional `REPORT_TOKEN`: release assembly still succeeds.
- Branch delivery: a push to `system-bars` runs the release workflow and exposes the signed APK.

---

### Task 1: System-bar regression fix

**Files:**
- Modify: `app/src/main/kotlin/com/gios/lightsports/MainActivity.kt`
- Modify: `app/build.gradle.kts`
- Test: `app/src/androidTest/kotlin/com/gios/lightsports/SystemBarsTest.kt`

**Interfaces:**
- Consumes: Android `WindowInsets.Type.systemBars()` and Compose `safeDrawingPadding()`.
- Produces: an `app-content` semantics node whose bounds clear both system bars.

- [ ] **Step 1: Verify the regression test fails without safe-drawing padding**

Run the API 35 instrumentation test with padding absent. Expected: FAIL because content top is `0`, inside the status bar.

- [ ] **Step 2: Apply safe-drawing padding to ordinary app content**

Apply `Modifier.fillMaxSize().safeDrawingPadding().testTag("app-content")` to the root content `Column`.

- [ ] **Step 3: Run the system-bar test**

Run: `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.gios.lightsports.SystemBarsTest`
Expected: PASS on the API 35 emulator.

### Task 2: Fork-owned release signing

**Files:**
- Modify: `signing-fingerprint.txt`
- Never add: `keystore/lightsports.jks`

**Interfaces:**
- Consumes: `KEYSTORE_B64` and `KEYSTORE_PASSWORD` repository secrets.
- Produces: release APKs signed by the certificate pinned in `signing-fingerprint.txt`.

- [ ] **Step 1: Generate a release keystore outside tracked files**

Use `keytool` with alias `lightsports`, a strong generated password, and a long validity period.

- [ ] **Step 2: Pin its SHA-256 certificate fingerprint**

Replace `signing-fingerprint.txt` with the generated certificate's SHA-256 digest.

- [ ] **Step 3: Create GitHub Actions secrets on the fork**

Set `KEYSTORE_B64` to the base64-encoded keystore and `KEYSTORE_PASSWORD` to its password using `gh secret set`; never print either value.

- [ ] **Step 4: Verify local signed release assembly**

Run `KEYSTORE_PASSWORD=<redacted> ./gradlew :app:assembleRelease`, then verify the APK certificate with `apksigner` against `signing-fingerprint.txt`.

### Task 3: Branch release workflow and fork

**Files:**
- Modify: `.github/workflows/build.yml`

**Interfaces:**
- Consumes: a push to `system-bars` and Task 2's repository secrets.
- Produces: upstream-style `LightSports-apk`/debug artifacts and a GitHub Release containing the signed APK.

- [ ] **Step 1: Add `system-bars` to the release workflow push branches**

Keep the existing manual dispatch, tests, signing checks, artifacts, and GitHub Release steps unchanged.

- [ ] **Step 2: Verify the complete local project**

Run: `./gradlew testDebugUnitTest connectedDebugAndroidTest assembleDebug`
Expected: BUILD SUCCESSFUL with all tests passing.

- [ ] **Step 3: Fork and configure remotes**

Create `MHolmes91/BrightSports`, preserve `gi-os/BrightSports` as `upstream`, and use the fork as `origin`.

- [ ] **Step 4: Commit and push `system-bars`**

Commit the approved source, tests, workflow, fingerprint, spec, and plan; push with upstream tracking.

- [ ] **Step 5: Verify GitHub Actions and release output**

Watch the branch workflow to completion, confirm success, and report the artifact/release URL for the APK.

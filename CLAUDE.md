# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A single-module Vaadin Flow add-on that wraps the **HugeRTE** rich text editor (an MIT-licensed fork of TinyMCE). It is a direct port of "TinyMCE for Flow" — comments and issue references may still mention TinyMCE. The component's value is **plain HTML** and it implements `HasValue`, so it works with Binder. Untrusted client HTML should be filtered by the app (e.g. a JSOUP converter); this add-on does not sanitize.

## Build & test

- **Build + run tests:** `mvn install` — compiles and runs the Playwright smoke test.
- **Run the demo app:** run `Application` (`src/test/java/.../Application.java`) — Spring Boot serves the test UIs at http://localhost:9998. Each demo view is a `@Route`.
- **Single test:** `mvn test -Dtest=MopoSmokeTest`
- **JDK 21, Vaadin 25** (`vaadin.version` in `pom.xml`). CI (`.github/workflows/ci-build.yml`) runs `mvn install javadoc:javadoc`.
- Release is automated via a tag push (`release-build.yml`) — do not run `release:perform` manually. See README for `mvn release:prepare`.

## Architecture

Two halves communicate as a Vaadin server component ↔ Lit web component:

- **`HugeRte.java`** (main component, ~570 lines) — extends `AbstractSinglePropertyField<HugeRte, String>`. The single synced property is the HTML value. Editor options are built into a Jackson (`tools.jackson`, i.e. Jackson 3) `ObjectNode` (`initialConfig`) and pushed to the client on attach. Config set after initialization throws `AlreadyInitializedException` / `ConfigurationConflictException`. Supporting enums/config types (`Plugin`, `Toolbar`, `Menubar`, `ValueChangeMode`, `ResizeDirection`, `Language`, `HugeRteVariant`) live alongside it in `src/main/java/org/vaadin/hugerte/`.
- **`vaadin-huge-rte.js`** (`src/main/resources/META-INF/resources/frontend/`) — a Lit element composed from Vaadin field mixins. It lazily boots the bundled HugeRTE engine and syncs value changes back to the server. Large value edits are sent as **diff-match-patch** patches (server side uses the `diff-match-patch` Java lib, client uses the `diff-match-patch` npm module) rather than full HTML, to keep round-trips small.
- **Bundled HugeRTE engine** — the entire HugeRTE JS distribution (`hugerte.min.js` + `langs/`, `plugins/`, `themes/`, ...) is checked into `src/main/resources/META-INF/resources/assets/hugerte_addon/` (served at `/assets/...`, which Vaadin's Spring Security integration whitelists by default — `/frontend/...` is not). It is **not** pulled via npm; the `@NpmPackage("hugerte", ...)` annotation on `HugeRte` is intentionally commented out. To update the editor, replace these files.

## Tests

All tests are in `src/test/java` and are also live demo views. `MopoSmokeTest` launches Playwright/Chromium and walks the demo routes, using the `mopo` (in.virit) helper. Some tests are known-flaky on CI and skipped by name inside the smoke test (e.g. `githubissue2`) — check the `flakyIgnoredTests` list before assuming a route is broken. Views named after issues (`Issue30`, `PreserveOnRefreshBug27`, `GitHubIssue2`) are regression reproductions.

## Conventions

`STYLEGUIDE.md` holds the coding standards (braces on all one-liners, blank-line phrasing of methods, no fully-qualified names, prefer official Vaadin API over DOM/JS workarounds). Lombok is available as an annotation processor.

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
sbt run          # Start dev server on http://localhost:9000 (auto-reloads on change)
sbt test         # Run all tests
sbt compile      # Compile without running
sbt dist         # Build production package (output: target/universal/)
```

Run a single test class:
```bash
sbt "testOnly controllers.HomeControllerSpec"
```

## Architecture

This is a **backend-only Play Framework 3 (Scala 3) REST API** — no views, no frontend. All responses are JSON.

- `app/controllers/` — HTTP layer. Controllers extend `BaseController`, inject `ControllerComponents`, and return `play.api.libs.json.Json` responses.
- `conf/routes` — URL-to-controller mappings. Add every new endpoint here.
- `conf/application.conf` — Play configuration. Secret key is read from `APPLICATION_SECRET` env var in production.
- `test/controllers/` — Controller tests using `scalatestplus-play`. Tests use `GuiceOneAppPerTest` + `FakeRequest` to hit routes without a running server.

## Git workflow

All development goes through pull requests into `dev`. Never commit directly to `dev` or `master`. Branch off `dev`, then open a PR targeting `dev`.

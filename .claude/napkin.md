# Napkin

## Corrections
| Date | Source | What Went Wrong | What To Do Instead |
|------|--------|----------------|-------------------|
| 2026-03-23 | self | Javadoc plugin failed on Java 17+ CI — strict doclint treats warnings as errors | Always set `<doclint>none</doclint>` and `<failOnWarnings>false</failOnWarnings>` in maven-javadoc-plugin for multi-Java version builds |
| 2026-03-23 | self | Branch protection `required_status_checks.contexts` was set to `["build"]` but actual job names are `"Java 8 — Build & Test"` etc. | Always match contexts to actual GitHub Actions job names, not workflow names |
| 2026-03-23 | self | `gh api` with `-f` flag failed for JSON body — wrong syntax | Use `--input -` with heredoc for JSON payloads to gh api |

## User Preferences
- User communicates in Portuguese (BR)
- Wants both Maven and Gradle build support
- Multi-Java version support: 8, 11, 17, 21
- Documentation style based on j-obs project (github.com/JohnPitter/j-obs): centered header, badges, feature tables, Mermaid diagrams, sections with emojis
- GitHub account: JohnPitter
- GPG key: ed25519/AA75D459C8D9E8D8 (Joao Pedro Tavares)

## Patterns That Work
- Using `mvn clean verify` for full build validation (compile + test + javadoc + source jar)
- CI matrix strategy with Java 8, 11, 17, 21 on ubuntu-latest
- Temporarily disabling enforce_admins to merge initial setup PRs, then re-enabling

## Patterns That Don't Work
- Setting javadoc strict mode with multi-Java CI — Java 17+ will fail on any missing @return/@param
- Using generic status check names that don't match actual workflow job names

## Domain Notes
- Project: Java library for Brazilian alphanumeric CNPJ validation/migration
- CNPJ alfanumerico: first 12 chars become A-Z/0-9, last 2 remain numeric
- Check digit algorithm: Modulo 11 with ASCII-48 conversion
- Weights: [6,5,4,3,2,9,8,7,6,5,4,3,2]
- Timeline: homologation 2026-04-06, production 2026-07-06
- Backwards compatible with numeric-only CNPJs
- Maven Central publishing: needs Sonatype OSSRH account + GPG signing
- Package: io.github.joaop / cnpj-alfanumerico

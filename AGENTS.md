# Agent Protocol — validation-api-utils

## Critical rule: No committing without explicit user approval

Agents must **never** commit code to any branch without the user's explicit review and approval.

- Agents may read files, run builds/tests, and propose changes.
- Agents may stage changes locally for review, but must **not** run `git commit` or `git push` unless the user has explicitly instructed them to do so.
- Before committing, the agent must present a summary of all changes and wait for the user to say "commit" or "approve".

## Project context

- This is a Maven multi-module Java project being migrated to Java 25.
- Parent module: `validation-api-utils` (pom packaging)
- Child module: `validation-rules-generator` (jar packaging)
- Build tool: Maven wrapper (`./mvnw`), Java 25 (Temurin)
- Migration guide: `UPGRADE_TO_JAVA25.md`

## Safe actions for agents (no approval needed)

- `mvnw clean compile` / `mvnw clean test`
- Reading source, pom, and documentation files
- Proposing edits via `edit`/`write` (leaves working tree modified, uncommitted)

## Actions requiring explicit user approval

- `git commit`
- `git push`

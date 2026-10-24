# Upgrade to JUnit 6 (JUnit Jupiter) using OpenRewrite

This document describes how to upgrade the validation-api-utils project from JUnit 4 to JUnit 6 (JUnit Jupiter) using OpenRewrite.

## Prerequisites

- Maven 3.9+
- Java 25 (for running the migrated project)
- Java 8+ (for running OpenRewrite)

## Migration Strategy — Step-by-Step Execution

The migration follows a 4-step process. Execute all steps in order:

### Step 1: JUnit 4 → JUnit 5 Migration

Replace JUnit 4 with JUnit Jupiter 5:

| Change | From | To |
|--------|------|-----|
| Dependency | `junit:junit:4.13` | `org.junit.jupiter:junit-jupiter:5.x` |
| Annotation | `org.junit.Test` | `org.junit.jupiter.api.Test` |
| Annotation | `org.junit.Before` | `org.junit.jupiter.api.BeforeEach` |
| Annotation | `org.junit.After` | `org.junit.jupiter.api.AfterEach` |
| Annotation | `org.junit.BeforeClass` | `org.junit.jupiter.api.BeforeAll` |
| Annotation | `org.junit.AfterClass` | `org.junit.jupiter.api.AfterAll` |
| Annotation | `org.junit.Ignore` | `org.junit.jupiter.api.Disabled` |
| Annotation | `org.junit.runner.RunWith` | `org.junit.jupiter.api.extension.ExtendWith` |
| Class | `org.junit.Assert` | `org.junit.jupiter.api.Assertions` |
| Static import | `org.junit.Assert.assert*` | `org.junit.jupiter.api.Assertions.assert*` |

**Assertion argument order:** JUnit 4 places the message **first**; JUnit Jupiter places it **last**.
- JUnit 4: `assertEquals("message", expected, actual)`
- JUnit 5: `assertEquals(expected, actual, "message")`

### Step 2: Apply JUnit 5 Best Practices

After migration, improve test code to follow JUnit 5 best practices:

- Use `@DisplayName` for descriptive test names
- Use `@Nested` for grouping related tests
- Use `@ParameterizedTest` with `@ValueSource`, `@CsvSource`, `@MethodSource` for repetitive test patterns
- Use `assertAll()` for grouped assertions
- Use `assertThrows()` for exception testing
- Use `assertTimeout()` / `assertTimeoutPreemptively()` for timing-sensitive tests
- Replace `@Rule` / `@ClassRule` with `@ExtendWith` extensions
- Use `@TempDir` for temporary directory management

### Step 3: JUnit 5 → JUnit 6 Migration (via OpenRewrite)

Upgrade from JUnit 5 to JUnit 6 using OpenRewrite:

**a) Configure the OpenRewrite recipe in parent `pom.xml`**

Add the JUnit 5 → JUnit 6 migration recipe to the `rewrite-maven-plugin` configuration:

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>6.46.1</version>
    <configuration>
        <recipeArtifactCoordinates>
            <recipeArtifactCoordinate>org.openrewrite.recipe:rewrite-migrate-java:3.42.1</recipeArtifactCoordinate>
        </recipeArtifactCoordinates>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.UpgradeToJava25</recipe>
            <recipe>org.openrewrite.java.migrate.junit5.JUnit5ToJUnit6</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

**b) Update the dependency version**

Change `junit-jupiter` version from `5.11.0` to `6.0.0` in `validation-rules-generator/pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>6.0.0</version>
    <scope>test</scope>
</dependency>
```

**c) Run OpenRewrite**

Execute from the project root:

```bash
./mvnw rewrite:run
```

**d) Manual fixes**

OpenRewrite may not catch everything. Manually fix any remaining JUnit 5 → 6 API changes:
- Updated imports
- Changed method signatures
- New/removed annotations
- Assertion API changes

### Step 4: Apply JUnit 6 Best Practices

After the JUnit 6 migration, apply JUnit 6-specific best practices:

- Use any new JUnit 6 annotations or APIs
- Leverage new assertion methods introduced in JUnit 6
- Use the new extension model features
- Apply any other JUnit 6 idioms and patterns

## Quick Start using OpenRewrite Maven Plugin

### Option 1: Run OpenRewrite directly

```bash
cd validation-api-utils

./mvnw org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:3.42.1 \
  -Drewrite.activeRecipes=org.openrewrite.java.migrate.junit5.JUnit5ToJUnit6
```

### Option 2: Add plugin permanently to parent pom.xml

Add the `rewrite-maven-plugin` with the JUnit 5 → JUnit 6 recipe (as shown in Step 3a above), then run:

```bash
./mvnw rewrite:run
```

## Verification

After migration, verify:

```bash
# Check JUnit version in child pom.xml
grep -A3 "junit-jupiter" validation-rules-generator/pom.xml

# Check for any remaining JUnit 4 imports
grep -r "org.junit.Assert\|org.junit.Test\|org.junit.Before\|org.junit.After\|org.junit.Ignore\|org.junit.RunWith" --include="*.java" .

# Build and test
./mvnw clean test

# Check effective dependency tree for JUnit
./mvnw dependency:tree | grep junit
```

## Constraints

- Agents must **never** commit code without explicit user approval.
- Agents may read files, run builds/tests, and propose changes.
- Agents may stage changes locally for review, but must **not** run `git commit` or `git push` unless explicitly instructed.
- Running OpenRewrite migration (`rewrite:run`) requires explicit user approval per AGENTS.md.
- `./mvnw clean test` is a safe action (no approval needed).
- Do not modify Java version settings (25) — that is a separate migration.

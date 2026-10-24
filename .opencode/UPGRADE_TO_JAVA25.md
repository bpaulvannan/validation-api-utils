# Upgrade to Java 25 using OpenRewrite

This document describes how to upgrade the validation-api-utils project from Java 8 to Java 25 using OpenRewrite.

## Prerequisites

- Maven 3.9+
- Java 25 (for running the migrated project)
- Java 8+ (for running OpenRewrite - it can run on Java 8+ but targets Java 25)

## Quick Start using OpenRewrite Maven Plugin

### Option 1: Run OpenRewrite directly (no agent needed)

```bash
cd validation-api-utils

# Add OpenRewrite plugin to parent pom.xml and run migration
mvn org.openrewrite.maven:rewrite-maven-plugin:5.43.0:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:2.14.0 \
  -Drewrite.activeRecipes=org.openrewrite.java.migrate.UpgradeToJava25
```

### Option 2: Add plugin permanently to pom.xml

Add this to the parent `pom.xml` under `<build><plugins>`:

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.43.0</version>
    <configuration>
        <recipeArtifactCoordinates>
            <recipeArtifactCoordinate>org.openrewrite.recipe:rewrite-migrate-java:2.14.0</recipeArtifactCoordinate>
        </recipeArtifactCoordinates>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.UpgradeToJava25</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

Then run:
```bash
mvn rewrite:run
```

## What the UpgradeToJava25 Recipe Does

The `org.openrewrite.java.migrate.UpgradeToJava25` composite recipe includes:

1. **UpgradeToJava21** (which includes UpgradeToJava17, UpgradeToJava11)
2. **UpgradeToJava25** specific changes:
   - Updates `maven.compiler.source` and `maven.compiler.target` to 25
   - Updates `java.toolchain.languageVersion` to 25
   - Updates `maven-compiler-plugin` to 3.13+
   - Replaces deprecated APIs with modern equivalents
   - Updates language features (patterns, records, switch expressions, etc.)
   - Updates standard library usage

## Manual Steps After Migration

After running OpenRewrite, you may need to:

1. **Update dependencies** - Some old dependencies may not work with Java 25:
   - `javax.validation:validation-api:1.0.0.GA` → `jakarta.validation:jakarta.validation-api:3.1.0`
   - `org.hibernate:hibernate-validator:4.1.0.Final` → `org.hibernate.validator:hibernate-validator:8.0.1.Final`
   - `org.slf4j:slf4j-simple:1.5.6` → `org.slf4j:slf4j-simple:2.0.13`
   - `commons-lang3:3.10` → `org.apache.commons:commons-lang3:3.15.0`
   - `junit:junit:4.13` → `org.junit.jupiter:junit-jupiter:5.11.0` (or keep JUnit 4 with `junit:junit:4.13.2`)

2. **Update package imports** - `javax.*` → `jakarta.*` for validation APIs

3. **Verify module-info.java** - If using modules, update exports/opens

4. **Run tests** - `mvn test` to ensure everything works

## Using the OpenCode Agent

If you have OpenCode installed, you can use the dedicated agent:

```bash
cd validation-api-utils
opencode -a java25-upgrade
```

The agent will:
1. Analyze the project structure
2. Add OpenRewrite plugin configuration
3. Run the migration recipe
4. Update dependencies
5. Verify the build passes

## Verification

After migration, verify:

```bash
# Check Java version in pom.xml
grep -r "25" pom.xml validation-rules-generator/pom.xml

# Build and test
mvn clean compile test

# Check effective Java version
mvn help:effective-pom | grep -A2 "<source>"
```

## Troubleshooting

### OpenRewrite requires Java 8+ to run
If you get errors about Java version, run Maven with Java 8+ but the recipe will target Java 25.

### Dependency conflicts
Run `mvn dependency:tree` to identify conflicts after migration.

### Compilation errors
Some code changes may be needed manually for APIs that OpenRewrite couldn't automatically migrate.
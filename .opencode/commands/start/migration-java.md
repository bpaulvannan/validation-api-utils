---
description: Start Java version migration using OpenRewrite
agent: java25-upgrade
---

Start the Java migration to version $ARGUMENTS using OpenRewrite.

This command will:
1. Analyze the project structure and current Java version configuration
2. Run the OpenRewrite recipe `org.openrewrite.java.migrate.UpgradeToJava$ARGUMENTS` to migrate the codebase
3. Update build configuration (maven.compiler.source, maven.compiler.target, java.toolchain.languageVersion)
4. Modernize Java syntax and replace deprecated APIs
5. Provide guidance on manual dependency updates needed after migration

Run the migration with: `mvn rewrite:run`

After migration, verify with: `mvn clean compile test`

Note: You may need to manually update outdated dependencies after the automated migration (e.g., javax → jakarta packages, old library versions).

Usage:
- `/start migration java25` - Migrate to Java 25
- `/start migration java21` - Migrate to Java 21
- `/start migration java17` - Migrate to Java 17
- `/start migration java11` - Migrate to Java 11
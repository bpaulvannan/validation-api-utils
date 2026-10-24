---
description: Start JUnit migration to JUnit 6 (JUnit Jupiter) using OpenRewrite
agent: junit6-upgrade
---

Start the JUnit migration to $ARGUMENTS using OpenRewrite.

This will:
1. Analyze the project structure and current JUnit version configuration
2. Run the OpenRewrite recipes to migrate JUnit 4 → JUnit 5/6:
   - `org.openrewrite.java.testing.junit5.JUnit5BestPractices`
   - `org.openrewrite.java.testing.junit5.JUnit4ToJUnit5`
   - `org.openrewrite.java.dependencies.UpgradeDependencyVersion` (to JUnit Jupiter 6.x)
3. Update test annotations, assertions, and dependencies
4. Modernize test patterns (parameterized tests, display names, etc.)

Run the migration with: `mvn rewrite:run`

After migration, verify with: `mvn clean test`

Usage:
- `/start migration junit6` - Migrate to JUnit 6 (JUnit Jupiter)
- `/start migration junit5` - Migrate to JUnit 5
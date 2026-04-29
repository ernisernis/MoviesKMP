# Version Compatibility

This project has several version layers. The important part is that they are not all the same thing.

## The Version Stack

```text
JDK runs Gradle
Gradle runs plugins
Plugins configure tasks
Tasks compile/package each platform
```

## Key Versions in This Project

They live in `gradle/libs.versions.toml`:

```toml
agp = "8.11.2"
kotlin = "2.3.20"
composeMultiplatform = "1.10.3"
```

And Gradle itself lives in:

```text
gradle/wrapper/gradle-wrapper.properties
```

## What Each Version Means

### Gradle version

Gradle is the build tool. It resolves dependencies, runs plugins, creates the task graph, and executes tasks.

You update it by changing the wrapper:

```text
distributionUrl=https://services.gradle.org/distributions/gradle-8.14.3-bin.zip
```

### AGP version

AGP means Android Gradle Plugin.

It controls Android-specific build behavior:

- `android {}` DSL
- Android manifests
- resources
- `compileSdk`, `minSdk`, `targetSdk`
- build variants like debug/release
- APK/AAB packaging
- D8/R8 Android compilation and shrinking

In the version catalog:

```toml
androidApplication = { id = "com.android.application", version.ref = "agp" }
androidLibrary = { id = "com.android.library", version.ref = "agp" }
```

### Kotlin version

The Kotlin version is the Kotlin Gradle Plugin version and the Kotlin compiler version used by your modules.

In this project:

```toml
kotlin = "2.3.20"
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
composeCompiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

This means KMP, JVM Kotlin, and the Compose compiler plugin are all aligned to the same Kotlin version.

### Compose Multiplatform version

Compose Multiplatform provides Compose UI libraries and Gradle integration for desktop/web/resources.

```toml
composeMultiplatform = "1.10.3"
compose-runtime = { module = "org.jetbrains.compose.runtime:runtime", version.ref = "composeMultiplatform" }
compose-ui = { module = "org.jetbrains.compose.ui:ui", version.ref = "composeMultiplatform" }
```

## Compatibility Checks

When upgrading, check these in order:

1. Does this Gradle version support the JDK running Gradle?
2. Does this Kotlin Gradle Plugin version support this Gradle version?
3. Does this Kotlin version support this AGP version?
4. Does this AGP version support this Gradle version?
5. Does this Compose Multiplatform version support this Kotlin version?
6. Does Android Studio support this AGP/Kotlin/KMP combination?

## Current Project Warning

This project currently has:

```toml
agp = "8.11.2"
kotlin = "2.3.20"
```

Kotlin's Gradle compatibility table says Kotlin Gradle Plugin `2.3.20` supports AGP `8.2.2` through `9.0.0`.

Android's Kotlin support table is stricter for Android D8/R8 support and says Kotlin `2.3` requires AGP `8.13.2`.

That means this pair is risky:

```text
Kotlin 2.3.20 + AGP 8.11.2
```

The safer options are:

```text
Upgrade AGP to 8.13.2 or newer
```

or:

```text
Downgrade Kotlin to a 2.2.x version compatible with AGP 8.11.2
```

## Practical Rule

Do not upgrade Kotlin, AGP, Gradle, and Compose randomly one by one.

Treat them as a set:

```text
Gradle wrapper
AGP
Kotlin
Compose Multiplatform
Android Studio
```

When one changes, check the compatibility tables before syncing.

Useful official references:

- Kotlin Gradle compatibility: https://kotlinlang.org/docs/gradle-configure-project.html
- Android Kotlin/AGP D8/R8 support: https://developer.android.com/build/kotlin-support
- Gradle compatibility: https://docs.gradle.org/current/userguide/compatibility.html
- AGP release notes: https://developer.android.com/build/releases/gradle-plugin

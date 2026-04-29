# How Gradle Starts

Gradle is the build system. It does not automatically know what an Android app, KMP library, iOS framework, server, or Compose app is. Plugins teach Gradle those concepts.

## The Startup Flow

When you run:

```shell
./gradlew :composeApp:assembleDebug
```

Gradle roughly does this:

1. Starts the Gradle wrapper from `gradlew`.
2. Uses the Gradle version from `gradle/wrapper/gradle-wrapper.properties`.
3. Reads `settings.gradle.kts`.
4. Loads plugin repositories and dependency repositories.
5. Includes modules like `:composeApp`, `:server`, and `:shared`.
6. Reads the root `build.gradle.kts`.
7. Reads each included module's `build.gradle.kts`.
8. Applies plugins to modules.
9. Builds a task graph.
10. Runs the requested task and the tasks it depends on.

## The Most Important Files

### `settings.gradle.kts`

This is the entry point for the build structure.

```kotlin
rootProject.name = "movieskmp"
include(":composeApp")
include(":server")
include(":shared")
```

`rootProject.name` becomes part of IDE module names. For example, Android Studio may create internal modules like `movieskmp.shared.main`.

`include(":shared")` tells Gradle that `shared/build.gradle.kts` is part of this build.

### Root `build.gradle.kts`

The root build file usually does not build an app itself. In this project it declares plugin versions once:

```kotlin
alias(libs.plugins.kotlinMultiplatform) apply false
```

`apply false` means "make this plugin available, but do not apply it to the root project."

### Module `build.gradle.kts`

These are where real behavior is configured:

```text
composeApp/build.gradle.kts
shared/build.gradle.kts
server/build.gradle.kts
```

The plugins applied in each module decide what that module can do.

## What Plugins Do

Plugins add tasks, configuration blocks, and dependency configurations.

Examples:

| Plugin | Adds |
|---|---|
| `org.jetbrains.kotlin.multiplatform` | `kotlin {}` block, KMP targets, source sets, Kotlin compile tasks |
| `com.android.application` | `android {}` block, APK/AAB packaging, Android variants |
| `com.android.library` | `android {}` block, Android library variants |
| `org.jetbrains.compose` | Compose Multiplatform resources, desktop app configuration |
| `org.jetbrains.kotlin.plugin.compose` | Compose compiler support for `@Composable` |
| `io.ktor.plugin` | Ktor/server packaging tasks |

Gradle is the engine. Plugins are the build logic.

## Task Names

Gradle tasks are addressed by module:

```shell
./gradlew :composeApp:assembleDebug
./gradlew :shared:compileKotlinJvm
./gradlew :server:run
```

`:` means "module path". So `:shared` is not KMP magic. It is just the module named `shared`.

The KMP plugin then creates platform-specific tasks inside that module.

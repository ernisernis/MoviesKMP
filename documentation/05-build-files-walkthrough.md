# Gradle File Walkthrough

This page walks through the important build files in this project.

## `settings.gradle.kts`

```kotlin
rootProject.name = "movieskmp"
```

Names the root build. Android Studio also uses this name when creating internal module names.

```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

Enables generated project accessors like:

```kotlin
implementation(projects.shared)
```

Without this, you would usually write:

```kotlin
implementation(project(":shared"))
```

```kotlin
pluginManagement { repositories { ... } }
```

Where Gradle looks for plugins.

Examples:

- Android Gradle Plugin
- Kotlin Gradle Plugin
- Compose Gradle Plugin

```kotlin
dependencyResolutionManagement { repositories { ... } }
```

Where Gradle looks for normal libraries.

Examples:

- AndroidX
- Ktor
- Compose runtime
- Kotlin coroutines

```kotlin
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
```

Helps Gradle resolve Java toolchains automatically when needed.

```kotlin
include(":composeApp")
include(":server")
include(":shared")
```

Adds modules to the build.

## Root `build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
}
```

This declares plugin versions once for the whole build.

`alias(libs.plugins.androidApplication)` points to `gradle/libs.versions.toml`.

`apply false` means:

```text
make this plugin available, but do not apply it to this root project
```

Submodules apply only the plugins they need.

## `composeApp/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}
```

This module is:

- Kotlin Multiplatform
- Android application
- Compose Multiplatform UI app

```kotlin
kotlin {
```

Starts KMP configuration.

```kotlin
androidTarget {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}
```

Adds Android as a Kotlin target and tells Kotlin to generate JVM 11 bytecode for Android Kotlin compilation.

```kotlin
jvm()
```

Adds a JVM desktop target.

```kotlin
js {
    browser()
    binaries.executable()
}
```

Adds JavaScript browser output and makes it runnable.

```kotlin
wasmJs {
    browser()
    binaries.executable()
}
```

Adds Wasm browser output and makes it runnable.

```kotlin
sourceSets {
```

Configures source sets and their dependencies.

```kotlin
androidMain.dependencies {
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
}
```

Android-only dependencies.

```kotlin
commonMain.dependencies {
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(projects.shared)
}
```

Dependencies visible to all `:composeApp` targets.

```kotlin
jvmMain.dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
}
```

Desktop JVM-only dependencies.

```kotlin
android {
```

Starts Android application configuration from AGP.

```kotlin
namespace = "ernxe.dev.movieskmp"
```

Namespace for generated Android code.

```kotlin
compileSdk = libs.versions.android.compileSdk.get().toInt()
```

Android SDK used to compile.

```kotlin
applicationId = "ernxe.dev.movieskmp"
```

Real installed app id. Only application modules have this.

```kotlin
dependencies {
    debugImplementation(libs.compose.uiTooling)
}
```

Top-level Gradle dependency block. This one is Android variant-specific, not KMP source-set-specific.

```kotlin
compose.desktop {
    application {
        mainClass = "ernxe.dev.movieskmp.MainKt"
    }
}
```

Compose Desktop app configuration.

## `shared/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}
```

This module is:

- Kotlin Multiplatform
- Android library

It is not a runnable Android app.

```kotlin
androidTarget { ... }
```

Adds Android output for the shared library.

```kotlin
listOf(
    iosArm64(),
    iosSimulatorArm64()
).forEach { iosTarget ->
    iosTarget.binaries.framework {
        baseName = "Shared"
        isStatic = true
    }
}
```

Adds iOS device and simulator targets and produces a framework named `Shared`.

```kotlin
jvm()
js { browser() }
wasmJs { browser() }
```

Adds JVM, JS, and Wasm variants of the shared library.

```kotlin
commonMain.dependencies {
    // put your Multiplatform dependencies here
}
```

This is where shared multiplatform libraries go.

```kotlin
android {
    namespace = "ernxe.dev.movieskmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}
```

Android library configuration. Required because the module has an Android target.

## `server/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}
```

This is a JVM-only server app.

```kotlin
application {
    mainClass.set("ernxe.dev.movieskmp.ApplicationKt")
}
```

Tells Gradle which JVM `main()` function starts the server.

```kotlin
dependencies {
    implementation(projects.shared)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
}
```

The server depends on `:shared` plus Ktor server libraries.

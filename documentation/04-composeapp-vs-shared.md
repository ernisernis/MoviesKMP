# `:composeApp` vs `:shared`

This is one of the most confusing parts at first:

```text
composeApp/build.gradle.kts has android { ... }
shared/build.gradle.kts also has android { ... }
```

They both have Android configuration because they both produce something for Android. But they produce different things.

## Short Version

| Module | Android plugin | Produces | Purpose |
|---|---|---|---|
| `:composeApp` | `com.android.application` | APK/AAB | Runnable Android app |
| `:shared` | `com.android.library` | Android library/AAR variant | Reusable shared code for Android |

## `:composeApp` Is the Android App

In `composeApp/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.androidApplication)
}
```

This applies:

```text
com.android.application
```

That means `:composeApp` can produce an Android application.

Its `android {}` block contains app-level settings:

```kotlin
android {
    namespace = "ernxe.dev.movieskmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ernxe.dev.movieskmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
}
```

Important fields:

| Field | Meaning |
|---|---|
| `namespace` | Package namespace for generated Android code like `R`. |
| `applicationId` | Real app id installed on a device. |
| `compileSdk` | Android API used to compile. |
| `minSdk` | Oldest Android version allowed. |
| `targetSdk` | Android behavior level the app targets. |
| `versionCode` | Integer version for Play Store/device updates. |
| `versionName` | Human-readable app version. |

Only an Android application module has `applicationId`, `versionCode`, and `versionName`.

## `:shared` Is an Android Library for Android

In `shared/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.androidLibrary)
}
```

This applies:

```text
com.android.library
```

That means `:shared` can produce an Android library variant.

Its `android {}` block looks smaller:

```kotlin
android {
    namespace = "ernxe.dev.movieskmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
```

Important difference: there is no `applicationId`. A library is not installed on a device by itself.

## Why Both Need `compileSdk`

Both modules compile Android code.

`composeApp` compiles Android app code:

```text
composeApp/src/androidMain
```

`shared` compiles Android shared-library code:

```text
shared/src/androidMain
```

Since both compile Android APIs, both need `compileSdk`.

## Why Both Need `minSdk`

Both modules can contain Android code that might use APIs available only on newer Android versions.

The library's `minSdk` helps AGP check whether library code is compatible with the consuming app.

Usually, keep library `minSdk` less than or equal to the app `minSdk`.

## Why `:shared` Is Not `:app`

`:shared` is just the module name chosen by the wizard.

It means:

```text
shared code module
```

It is common in KMP projects because one module often contains business logic shared across Android, iOS, desktop, web, and server.

You could rename it to `:core`, `:domain`, or `:library` if you changed all references. KMP does not require the name `shared`.

## How `:composeApp` Uses `:shared`

In `composeApp/build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation(projects.shared)
}
```

Because this dependency is in `commonMain`, every target of `:composeApp` depends on the matching variant of `:shared`.

Examples:

```text
composeApp Android -> shared Android variant
composeApp JVM -> shared JVM variant
composeApp JS -> shared JS variant
composeApp Wasm -> shared Wasm variant
```

Gradle and the KMP plugin do the platform matching.

## How iOS Uses `:shared`

iOS does not use `:composeApp` in your intended setup.

Instead:

```text
iosApp SwiftUI app
  imports Shared framework
    built from :shared
```

In `shared/build.gradle.kts`:

```kotlin
iosTarget.binaries.framework {
    baseName = "Shared"
    isStatic = true
}
```

This is why Swift can do:

```swift
import Shared
```

## Mental Model

Use this split:

```text
:composeApp = runnable Compose app for Android/Desktop/Web
:shared = reusable KMP library for shared logic and iOS framework
iosApp = native SwiftUI iOS app
:server = JVM backend that can also reuse :shared
```

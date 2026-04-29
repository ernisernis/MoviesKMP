# KMP Project Structure

KMP means Kotlin Multiplatform. A KMP module can compile Kotlin code to more than one target.

## Modules in This Project

```text
:composeApp
:shared
:server
iosApp
```

Only the first three are Gradle modules. `iosApp` is an Xcode project.

## `:composeApp`

This is the runnable Compose Multiplatform UI app.

It targets:

- Android
- Desktop JVM
- JS browser
- Wasm browser

Its Gradle file is:

```text
composeApp/build.gradle.kts
```

## `:shared`

This is a KMP library module.

It targets:

- Android
- iOS device
- iOS simulator
- JVM
- JS browser
- Wasm browser

Its Gradle file is:

```text
shared/build.gradle.kts
```

The iOS app imports this module as a framework named `Shared`.

## `:server`

This is a JVM-only Ktor server.

It depends on `:shared`, so it can reuse common business/data code.

## `iosApp`

This is not a Gradle module. It is an Xcode app.

The iOS app uses SwiftUI and imports:

```swift
import Shared
```

The `Shared` framework is built by Gradle from `:shared`.

## Targets

A target is a platform output.

Examples from `:shared`:

```kotlin
androidTarget()
iosArm64()
iosSimulatorArm64()
jvm()
js { browser() }
wasmJs { browser() }
```

Each target creates platform-specific compilation tasks.

## Source Sets

A source set is a group of code and dependencies that compile together.

Common examples:

```text
commonMain
commonTest
androidMain
jvmMain
iosMain
jsMain
wasmJsMain
```

If code is in `commonMain`, it must work on every target in that module.

If code is in `androidMain`, it can use Android APIs but only compiles for Android.

## Why Source Sets Matter

This is valid in `androidMain`:

```kotlin
import android.content.Context
```

This is not valid in `commonMain`, because iOS, JS, Wasm, and JVM desktop do not have Android `Context`.

KMP protects you from accidentally using platform-only APIs in shared code.

## Expected Flow

Usually:

```text
shared/commonMain
  common models, business rules, networking abstractions

shared/androidMain
  Android-specific implementations

shared/iosMain
  iOS-specific implementations

composeApp/commonMain
  Compose UI shared by Android/Desktop/Web

iosApp
  Native SwiftUI iOS UI
```

In your intended architecture, iOS UI is native SwiftUI, while Android/Desktop/Web use Compose Multiplatform.

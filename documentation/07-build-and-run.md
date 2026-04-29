# Build and Run

This page lists useful commands for this project.

## Android App

Build debug APK:

```shell
./gradlew :composeApp:assembleDebug
```

The Android app comes from `:composeApp`, not `:shared`.

## Desktop JVM App

Run desktop app:

```shell
./gradlew :composeApp:run
```

The main class is configured in `composeApp/build.gradle.kts`:

```kotlin
mainClass = "ernxe.dev.movieskmp.MainKt"
```

## Web Wasm App

Run Wasm browser app:

```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Wasm is the modern web target.

## Web JS App

Run JS browser app:

```shell
./gradlew :composeApp:jsBrowserDevelopmentRun
```

JS can be useful when Wasm browser support or tooling is not enough.

## Server

Run Ktor server:

```shell
./gradlew :server:run
```

The server main class is configured in `server/build.gradle.kts`.

## iOS

Open:

```text
iosApp
```

in Xcode and run the app.

The Xcode project calls Gradle:

```shell
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

That builds the `Shared` framework from the `:shared` module.

## Explore Tasks

List all projects:

```shell
./gradlew projects
```

List tasks for the app module:

```shell
./gradlew :composeApp:tasks
```

List tasks for the shared module:

```shell
./gradlew :shared:tasks
```

## Read Task Names

Task names usually reveal the platform:

```text
compileKotlinJvm
compileDebugKotlinAndroid
wasmJsBrowserDevelopmentRun
jsBrowserDevelopmentRun
embedAndSignAppleFrameworkForXcode
```

KMP creates many tasks because each target has its own compilation and packaging needs.

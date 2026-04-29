# MoviesKMP

This repository is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop JVM, and a JVM server.

The links below are written as learning notes for this project. They explain how Gradle starts, how Kotlin Multiplatform maps code to platforms, how versions fit together, and why modules like `:composeApp` and `:shared` both contain Android configuration.

## Learning Guide

| Topic | Start here |
|---|---|
| Gradle mental model | [How Gradle Starts](documentation/01-gradle-mental-model.md) |
| Gradle, AGP, Kotlin, Compose versions | [Version Compatibility](documentation/02-version-compatibility.md) |
| KMP modules, targets, and source sets | [KMP Project Structure](documentation/03-kmp-project-structure.md) |
| `:composeApp` vs `:shared` | [App Module vs Shared Module](documentation/04-composeapp-vs-shared.md) |
| Line-by-line build files | [Gradle File Walkthrough](documentation/05-build-files-walkthrough.md) |
| Dependency placement | [Dependencies in KMP](documentation/06-dependencies.md) |
| Build and run commands | [Build and Run](documentation/07-build-and-run.md) |

## Project Map

| Path | Purpose |
|---|---|
| [`settings.gradle.kts`](settings.gradle.kts) | Defines the Gradle build and included modules. |
| [`gradle/libs.versions.toml`](gradle/libs.versions.toml) | Central version catalog for plugin and library versions. |
| [`build.gradle.kts`](build.gradle.kts) | Root build file that declares shared plugin versions. |
| [`composeApp`](composeApp) | Runnable Compose Multiplatform app for Android, Desktop JVM, JS, and Wasm. |
| [`shared`](shared) | Kotlin Multiplatform library shared by app/server/iOS. |
| [`server`](server) | JVM Ktor server module. |
| [`iosApp`](iosApp) | Native iOS Xcode/SwiftUI app that consumes the `Shared` framework from `:shared`. |

## Quick Commands

```shell
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:run
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
./gradlew :composeApp:jsBrowserDevelopmentRun
./gradlew :server:run
```

For iOS, open [`iosApp`](iosApp) in Xcode and run the app. Xcode calls Gradle to build `:shared` as an iOS framework.

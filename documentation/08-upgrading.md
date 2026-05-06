# Upgrading This KMP Project

This project uses the same upgrade logic as a modern Android-only Gradle project, plus a few extra compatibility checks for Kotlin Multiplatform, Compose Multiplatform, Native/iOS, JS, and Wasm.

The short version:

1. Upgrade one layer at a time.
2. Check compatibility before changing versions.
3. Keep Kotlin and the Compose compiler plugin on the same version.
4. Run Gradle sync and builds after each meaningful step.

## Where Versions Live

Most versions are centralized in:

```text
gradle/libs.versions.toml
```

The Gradle wrapper version lives in:

```text
gradle/wrapper/gradle-wrapper.properties
```

Current important versions:

```toml
kotlin = "2.3.20"
agp = "8.11.2"
composeMultiplatform = "1.10.3"
composeHotReload = "1.0.0"
ktor = "3.4.1"
android-compileSdk = "36"
android-targetSdk = "36"
android-minSdk = "24"
```

Current Gradle wrapper:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14.3-bin.zip
```

## The Compatibility Chain

Think of upgrades as a chain:

```text
JDK used to run Gradle
  -> Gradle wrapper
  -> Android Gradle Plugin
  -> Kotlin Gradle Plugin
  -> Compose compiler plugin
  -> Compose Multiplatform libraries
  -> AndroidX / Ktor / other libraries
  -> compileSdk / targetSdk
  -> Xcode for iOS targets
```

If one link is too old or too new, the build can fail even if the code is correct.

For Android-only projects, the main chain is usually:

```text
JDK -> Gradle -> AGP -> Kotlin -> Compose compiler -> AndroidX
```

For this KMP project, add:

```text
Kotlin Multiplatform plugin -> Compose Multiplatform -> Xcode / Native / JS / Wasm support
```

## Kotlin And Compose Compiler

In Kotlin 2.0 and newer, the Compose compiler is released with Kotlin.

This project correctly uses:

```toml
kotlin = "2.3.20"

[plugins]
composeCompiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
```

That means when you upgrade Kotlin, the Compose compiler plugin upgrades with it.

Manual Kotlin upgrade:

```toml
[versions]
kotlin = "NEW_VERSION"
```

Do not set the Compose compiler plugin to a different Kotlin version unless you have a very specific reason and know the compatibility risk.

## Compose Multiplatform

Compose Multiplatform is separate from Kotlin. It has its own version:

```toml
composeMultiplatform = "1.10.3"
```

The general rule is:

```text
Kotlin version must be supported by Compose Multiplatform.
Compose compiler plugin version must match Kotlin.
Compose Multiplatform libraries should be upgraded together.
```

This project uses Compose Multiplatform dependencies like:

```toml
compose-runtime = { module = "org.jetbrains.compose.runtime:runtime", version.ref = "composeMultiplatform" }
compose-foundation = { module = "org.jetbrains.compose.foundation:foundation", version.ref = "composeMultiplatform" }
compose-ui = { module = "org.jetbrains.compose.ui:ui", version.ref = "composeMultiplatform" }
compose-components-resources = { module = "org.jetbrains.compose.components:components-resources", version.ref = "composeMultiplatform" }
compose-uiToolingPreview = { module = "org.jetbrains.compose.ui:ui-tooling-preview", version.ref = "composeMultiplatform" }
```

Material 3 currently has its own version:

```toml
material3 = "1.10.0-alpha05"
compose-material3 = { module = "org.jetbrains.compose.material3:material3", version.ref = "material3" }
```

When upgrading Compose Multiplatform, check whether Material 3 has a matching recommended version.

## Android Gradle Plugin

AGP is used by:

```toml
androidApplication = { id = "com.android.application", version.ref = "agp" }
androidLibrary = { id = "com.android.library", version.ref = "agp" }
```

Manual AGP upgrade:

```toml
[versions]
agp = "NEW_VERSION"
```

Do not upgrade AGP by only looking at the newest version number. Open the official AGP release notes first:

```text
https://developer.android.com/build/releases/gradle-plugin
```

Each AGP release page has a `Compatibility` section. Read that section before editing this project. It tells you the supported or required:

```text
Gradle version
SDK Build Tools version
NDK version, if native code is involved
JDK version used to run Gradle
Maximum supported Android API level
```

For example, the AGP `9.2.0` release notes list:

```text
Gradle: minimum 9.4.1
SDK Build Tools: 36.0.0
NDK default: 28.2.13676358
JDK: 17
Maximum supported Android API level: 36.1
```

This is exactly the kind of table you must check for whatever AGP version you want to use. Do not assume these numbers stay the same across AGP releases.

Before changing AGP, check all of this:

```text
1. Gradle wrapper compatibility
   Does the target AGP version require a newer Gradle wrapper?
   Check gradle/wrapper/gradle-wrapper.properties.

2. JDK used to run Gradle
   Does the target AGP version require JDK 17, 21, or something else?
   Check Android Studio Gradle JDK and ./gradlew --version.

3. Kotlin Gradle Plugin compatibility
   Does your Kotlin version support that AGP version?
   Check Kotlin's Gradle compatibility table.

4. Kotlin Multiplatform compatibility
   Does KMP support that AGP version?
   This matters more here than in Android-only projects because shared uses kotlinMultiplatform plus androidTarget.

5. Android Studio compatibility
   Can your Android Studio version sync projects using that AGP version?
   Check the Android Studio and AGP compatibility table.

6. compileSdk / targetSdk compatibility
   Does the AGP version support the Android API level you compile or target?
   New API levels can require newer AGP and Android Studio versions.

7. SDK Build Tools / NDK
   Does the AGP version require or default to newer SDK Build Tools or NDK?
   NDK matters mostly if you add native/C++ dependencies.

8. Breaking behavior changes
   Read removed features, DSL changes, R8/D8 changes, lint changes, and default behavior changes in the release notes.

9. Android Gradle Plugin Upgrade Assistant
   For major AGP jumps, prefer Android Studio's AGP Upgrade Assistant because it can preserve or migrate changed defaults.

10. KMP Android plugin model
    AGP 9.0+ changes how Android and Kotlin plugins interact.
    A module that applies both com.android.application and org.jetbrains.kotlin.multiplatform can fail during sync.
    A module that applies both com.android.library and org.jetbrains.kotlin.multiplatform should migrate to com.android.kotlin.multiplatform.library.
```

AGP often forces a minimum Gradle version and a minimum JDK version. AGP 8.x and 9.x require running Gradle with JDK 17 or newer. This is separate from this project's `JvmTarget.JVM_11` bytecode target. You can run Gradle on JDK 17 while still compiling app/library bytecode to Java 11.

This project currently has:

```text
AGP: 8.11.2
Gradle wrapper: 8.14.3
Kotlin: 2.3.20
compileSdk: 36
targetSdk: 36
JVM bytecode target: 11
```

With Kotlin `2.3.20`, Kotlin's compatibility table says the fully supported AGP range is `8.2.2-9.0.0`, and the fully supported Gradle range is `7.6.3-9.3.0`. That means an AGP version newer than `9.0.0` may require also upgrading Kotlin, even if AGP itself supports your Gradle/JDK combination.

### AGP 9 And KMP Modules

AGP 9.0+ has a specific KMP migration issue.

If sync fails with:

```text
The 'com.android.library' (or 'com.android.application') plugin is not compatible with the 'org.jetbrains.kotlin.multiplatform' plugin since AGP 9.0.
```

then the problem is not JDK, Gradle wrapper, SDK Build Tools, or `compileSdk`. The problem is the Android plugin model used by the module.

For KMP library modules, Android recommends replacing:

```kotlin
alias(libs.plugins.androidLibrary)
alias(libs.plugins.kotlinMultiplatform)
```

with the Android-KMP library plugin:

```kotlin
id("com.android.kotlin.multiplatform.library")
alias(libs.plugins.kotlinMultiplatform)
```

But this only applies to KMP library modules. Android's documentation says there is not a direct replacement for a Kotlin Multiplatform module using `com.android.application`. To migrate an application module, extract the Android application into a separate Android app module and keep the KMP code in a library module.

For this project, that matters because:

```text
composeApp uses com.android.application + kotlinMultiplatform
shared uses com.android.library + kotlinMultiplatform
```

So an AGP 9 upgrade is not just a version bump. It can require restructuring modules.

The temporary bypass from the error message is:

```properties
android.builtInKotlin=false
android.newDsl=false
```

Use that only as a temporary migration aid. It opts out of AGP 9's new Kotlin/DSL behavior and does not remove the need to eventually migrate away from the old plugin combination.

## Gradle Wrapper

The Gradle wrapper is changed in:

```text
gradle/wrapper/gradle-wrapper.properties
```

Manual edit:

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-NEW_VERSION-bin.zip
```

Preferred command:

```bash
./gradlew wrapper --gradle-version NEW_VERSION --distribution-type bin
```

Then verify:

```bash
./gradlew --version
```

Before upgrading Gradle, check:

```text
Does Kotlin Gradle Plugin support this Gradle version?
Does AGP support this Gradle version?
Does Gradle support the JDK used to run the build?
```

## JVM / JDK Versions

There are two different JVM concepts:

1. The JDK used to run Gradle.
2. The JVM bytecode target used by Kotlin/Java compilation.

This project compiles Android/JVM code to Java 11 bytecode:

```kotlin
compilerOptions {
    jvmTarget.set(JvmTarget.JVM_11)
}
```

and:

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

That does not mean Gradle itself runs on Java 11. Modern AGP generally requires running Gradle with JDK 17 or newer.

If you want to upgrade bytecode target from 11 to 17, update every relevant module consistently:

```kotlin
compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
}
```

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

Do this only after checking Android desugaring/library compatibility and whether all targets still build.

## Android SDK Versions

These live in:

```toml
android-compileSdk = "36"
android-minSdk = "24"
android-targetSdk = "36"
```

Meaning:

```text
compileSdk: Android API used to compile against.
targetSdk: Android behavior level your app opts into.
minSdk: Oldest Android version your app supports.
```

Usually:

```text
compileSdk should be latest stable or required by dependencies.
targetSdk should be updated deliberately because behavior changes can affect runtime.
minSdk should only be raised if you are willing to drop older devices.
```

## Ktor And Server

Ktor is separate from Android/KMP tooling:

```toml
ktor = "3.4.1"
```

The server module uses:

```kotlin
alias(libs.plugins.kotlinJvm)
alias(libs.plugins.ktor)
application
```

When upgrading Ktor:

```text
Update ktor version.
Check Ktor migration notes.
Run server tests.
Run the server locally.
```

## AndroidX And JetBrains AndroidX

This project uses AndroidX and JetBrains multiplatform AndroidX artifacts:

```toml
androidx-activity = "1.13.0"
androidx-lifecycle = "2.10.0"
```

Examples:

```toml
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activity" }
androidx-lifecycle-viewmodelCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "androidx-lifecycle" }
androidx-lifecycle-runtimeCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose", version.ref = "androidx-lifecycle" }
```

Do not assume regular AndroidX and JetBrains multiplatform AndroidX artifacts always release on the same schedule. Check their release notes when upgrading.

## Safe Upgrade Order

For small patch upgrades:

1. Update one version in `gradle/libs.versions.toml`.
2. Sync Gradle.
3. Build.
4. Commit if it works.

For larger upgrades:

1. Upgrade Android Studio / IntelliJ first.
2. Upgrade Gradle wrapper if required.
3. Upgrade AGP.
4. Upgrade Kotlin.
5. Upgrade Compose compiler automatically through Kotlin.
6. Upgrade Compose Multiplatform.
7. Upgrade AndroidX / lifecycle / Ktor / other libraries.
8. Upgrade compileSdk.
9. Upgrade targetSdk separately and test Android runtime behavior.
10. Check iOS with Xcode if `shared` still has iOS targets.

Do not upgrade every major tool at once unless you are prepared to debug several compatibility failures at the same time.

## Manual Upgrade Example: Kotlin

Suppose you want to upgrade Kotlin from `2.3.20` to `2.3.21`.

Edit:

```toml
[versions]
kotlin = "2.3.21"
```

Because these plugin aliases use `version.ref = "kotlin"`, they upgrade automatically:

```toml
composeCompiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

Then run:

```bash
./gradlew clean build
```

If that is too broad while iterating, start with:

```bash
./gradlew :shared:build
./gradlew :composeApp:build
./gradlew :server:build
```

## Manual Upgrade Example: Compose Multiplatform

Edit:

```toml
[versions]
composeMultiplatform = "NEW_VERSION"
```

Then check whether this should also change:

```toml
material3 = "NEW_VERSION_OR_RECOMMENDED_ALPHA"
```

Then run:

```bash
./gradlew :composeApp:build
```

For UI changes, also run the actual targets you care about:

```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:jvmRun
```

Web/Wasm task names can vary by plugin version, so inspect available tasks when needed:

```bash
./gradlew :composeApp:tasks
```

## Manual Upgrade Example: AGP

Use this sequence when upgrading AGP.

First, choose the target AGP version from the official release notes:

```text
https://developer.android.com/build/releases/gradle-plugin
```

Then write down the compatibility row for that AGP version:

```text
Target AGP:
Required Gradle:
Required JDK:
Supported max API level:
Required/default SDK Build Tools:
Required/default NDK:
Breaking changes to check:
```

Compare it to the project:

```text
gradle/libs.versions.toml
gradle/wrapper/gradle-wrapper.properties
Android Studio Gradle JDK setting
./gradlew --version
```

If AGP requires a newer Gradle wrapper, update Gradle first:

```bash
./gradlew wrapper --gradle-version NEW_GRADLE_VERSION --distribution-type bin
```

Then verify:

```bash
./gradlew --version
```

Make sure the output shows a JDK supported by AGP. If AGP says `JDK 17`, Gradle must run on JDK 17 or newer, but not newer than the current Gradle version supports.

Then edit AGP:

```toml
[versions]
agp = "NEW_VERSION"
```

If the AGP upgrade also requires a newer Kotlin version, edit Kotlin too:

```toml
[versions]
kotlin = "NEW_KOTLIN_VERSION"
```

In this project, changing `kotlin` also changes:

```toml
composeCompiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

Do not change `JvmTarget.JVM_11` just because AGP requires JDK 17. The JDK that runs Gradle and the bytecode target are different decisions.

After editing, run:

```bash
./gradlew :shared:assembleDebug
./gradlew :composeApp:assembleDebug
```

Then run a broader build:

```bash
./gradlew clean build
```

If the upgrade is a major AGP version, also:

```text
Run Android Studio sync.
Open the AGP Upgrade Assistant and inspect suggested migrations.
Read release notes for removed DSL/features.
Check release build if R8/shrinker behavior changed.
Build iOS from Xcode if shared KMP code changed or Kotlin was upgraded.
```

## Manual Upgrade Example: JVM Target

If you decide to compile JVM/Android code to Java 17 bytecode, update all modules that currently use Java 11 settings.

Search for:

```text
JvmTarget.JVM_11
JavaVersion.VERSION_11
```

Change to:

```text
JvmTarget.JVM_17
JavaVersion.VERSION_17
```

Then run:

```bash
./gradlew clean build
```

This is a source/bytecode compatibility decision, not just a version bump.

## What To Check After Upgrading

Run:

```bash
./gradlew clean build
```

For Android:

```bash
./gradlew :composeApp:assembleDebug
./gradlew :shared:assembleDebug
```

For server:

```bash
./gradlew :server:test
```

For task discovery:

```bash
./gradlew tasks
./gradlew :composeApp:tasks
./gradlew :shared:tasks
```

If iOS is part of the change, also open/build the iOS app in Xcode because Gradle can pass while Xcode integration or framework linking still needs attention.

## Common Failure Patterns

### Kotlin upgraded but Compose compiler not upgraded

In this project that should not happen because `composeCompiler` uses `version.ref = "kotlin"`.

If you ever hardcode versions, avoid this mismatch:

```text
Kotlin 2.3.x with Compose compiler 2.2.x
```

### Gradle upgraded beyond AGP/Kotlin support

Symptom:

```text
Unsupported Gradle version
```

or plugin resolution/configuration errors.

Fix:

```text
Either upgrade AGP/Kotlin to versions that support the Gradle wrapper,
or lower the Gradle wrapper.
```

### AGP upgraded beyond KMP support

KMP has its own compatibility table for Kotlin Gradle Plugin, Gradle, AGP, and Xcode. Check it before jumping to a new AGP major version.

### Android Studio too old

Android Studio may refuse to sync a project with a newer AGP. Upgrade Android Studio or use an AGP version supported by your IDE.

### iOS breaks after Kotlin upgrade

Kotlin/Native and Xcode compatibility matter. Check the KMP compatibility table and rebuild the iOS framework from a clean state.

## Official References

Use these before doing major upgrades:

- Kotlin Gradle plugin compatibility: https://kotlinlang.org/docs/gradle-configure-project.html
- Kotlin Multiplatform compatibility: https://kotlinlang.org/docs/multiplatform/multiplatform-compatibility-guide.html
- Compose Multiplatform compatibility: https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html
- Compose compiler updates: https://kotlinlang.org/docs/multiplatform/compose-compiler.html
- Android Gradle Plugin releases: https://developer.android.com/build/releases/gradle-plugin
- Android Studio and AGP compatibility: https://developer.android.com/build/releases/about-agp
- Gradle compatibility matrix: https://docs.gradle.org/current/userguide/compatibility.html

## Practical Rule

For this project, most upgrades start in:

```text
gradle/libs.versions.toml
```

But the decision must be made from compatibility tables, not from version numbers alone.

If the upgrade touches Kotlin, AGP, Gradle, Compose Multiplatform, or Xcode, treat it as a tooling upgrade and test all affected targets. If it only touches a normal library, treat it like a regular dependency upgrade and run the modules that use it.

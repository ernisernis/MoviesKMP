# Dependencies in KMP

Dependencies in KMP are placed by source set.

That is different from a regular Android-only project, where you often place everything in one top-level `dependencies {}` block.

## Basic Rule

Put the dependency where the code that uses it lives.

| Code location | Dependency location |
|---|---|
| `commonMain` | `commonMain.dependencies` |
| `androidMain` | `androidMain.dependencies` |
| `jvmMain` | `jvmMain.dependencies` |
| `iosMain` | `iosMain.dependencies` |
| `jsMain` | `jsMain.dependencies` |
| Android debug tooling | top-level `debugImplementation` |

## Common Dependencies

Use `commonMain.dependencies` only for libraries that support all targets in that module.

Example:

```kotlin
commonMain.dependencies {
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
}
```

If a dependency is added to `commonMain`, code in `commonMain` can use it.

But it must be available for every target.

## Android Dependencies

Use `androidMain.dependencies` for Android-only libraries:

```kotlin
androidMain.dependencies {
    implementation(libs.androidx.activity.compose)
}
```

Code in `androidMain` can use Android APIs and AndroidX APIs.

Code in `commonMain` cannot.

## JVM Desktop Dependencies

Use `jvmMain.dependencies` for desktop/JVM-only libraries:

```kotlin
jvmMain.dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
}
```

## Project Dependencies

This project uses:

```kotlin
implementation(projects.shared)
```

That comes from:

```kotlin
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

It is equivalent to:

```kotlin
implementation(project(":shared"))
```

When used in `commonMain`, Gradle and the KMP plugin choose the matching platform variant.

Example:

```text
:composeApp Android target -> :shared Android variant
:composeApp JVM target -> :shared JVM variant
:composeApp JS target -> :shared JS variant
:composeApp Wasm target -> :shared Wasm variant
```

## `implementation` vs `api`

Most of the time use:

```kotlin
implementation(...)
```

It means "I use this dependency internally."

Use:

```kotlin
api(...)
```

only when the dependency appears in your public API.

Example:

```kotlin
class Repository(
    private val client: HttpClient
)
```

If `HttpClient` is from Ktor and this class is public from `:shared`, then consumers may need to know about Ktor. That can be a reason to use `api`, but keep it rare.

## Version Catalog

Library aliases come from:

```text
gradle/libs.versions.toml
```

Example:

```toml
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activity" }
```

Then Gradle uses:

```kotlin
implementation(libs.androidx.activity.compose)
```

Gradle converts the TOML alias name from kebab case to dot access.

## Common Mistakes

Do not put Android-only dependencies in `commonMain`:

```kotlin
commonMain.dependencies {
    implementation(libs.androidx.activity.compose) // wrong
}
```

Do this instead:

```kotlin
androidMain.dependencies {
    implementation(libs.androidx.activity.compose)
}
```

Do not expect iOS to use Gradle dependencies for Swift packages. Native iOS dependencies usually belong in Xcode/Swift Package Manager/CocoaPods, unless you are using Kotlin/Native libraries from Gradle.

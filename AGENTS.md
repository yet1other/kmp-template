# AGENTS.md: KmpTemplate

A Kotlin Multiplatform app for **Android and iOS** with a shared **Compose Multiplatform** UI. The base is the
JetBrains KMP wizard (Android + iOS, shared Compose UI, tests); the build conventions, `core/` modules and
architecture are the ones every project of ours uses (Decompose + MVIKotlin + Metro).

The `Note` feature (`core/domain/.../note`, `feature/home`) is a sample that runs through every layer.
Replace it with the real domain; keep the shape.

## Principles

Clean Architecture, SOLID, KISS, YAGNI. In practice:

- Build only what a feature needs today. No speculative abstractions, modules or targets.
- Dependencies point inward: `feature → core:domain ← core:data`. Features never see data or database types.
- All logic below the UI (components, stores, repositories) is shared Kotlin; the UI is shared Compose.
- One responsibility per module; a module exposes interfaces, not its implementation details.

## Platforms and modules

| Gradle path | Directory | Role |
|---|---|---|
| `:androidApp` | `app/androidApp` | Android entry point: `KmpTemplateApplication` owns the graph, `MainActivity` the retained root |
| — | `app/iosApp` | iOS app (SwiftUI shell). `AppDelegate` owns `IosApp` (from the static `SharedUI` framework) and forwards state restoration to it; the build phase runs `:sharedUI:embedAndSignAppleFrameworkForXcode` |
| `:sharedUI` | `shared/ui` | Compose UI for both platforms: `App(root)`, one `XxxContent` per component. `iosMain` holds only the UIKit entry `IosApp` (`ApplicationLifecycle`, `ComposeUIViewController`) |
| `:sharedLogic` | `shared/logic` | The Metro application graph: `AppGraph` (common) + `AndroidAppGraph` / `NativeAppGraph` with the platform bindings. `nativeMain`: `AppRoot` (graph, root component, `StateKeeperDispatcher`) and `StateKeeperUtils` (save/restore through `NSCoder`) |
| `:core:common` | `core/common` | `AppDispatchers`, `Log` (Kermit), Decompose/MVIKotlin glue (`asValue`, `combine`, `coroutineScope`, `PreviewComponentContext`), `CommonBindings` (StoreFactory, dispatchers) |
| `:core:domain` | `core/domain` | Entities, value classes, repository interfaces, pure rules. Plain Kotlin |
| `:core:data` | `core/data` | Repository implementations over the database (and the network when there is one) |
| `:core:database` | `core/database` | SQLDelight `AppDatabase`, DAO interfaces, `SqlDriverFactory` (platform drivers) |
| `:core:resources` | `core/resources` | Compose resources (strings, images; `Res` is public) and `AppTheme` |
| `:feature:root` | `feature/root` | `RootComponent`: the child stack of full-screen destinations |
| `:feature:home` | `feature/home` | The sample feature: component, store, mapper, bindings |

KMP targets: Android, `iosArm64`, `iosSimulatorArm64` (set once in `configureKotlinMultiplatform`).

Apple code goes in `nativeMain`, not `iosMain`, so a `macosArm64` target can be added later by adding it in
`configureKotlinMultiplatform` alone. Only UIKit-bound code stays in `iosMain` (today: `IosApp` in `:sharedUI`,
because `ComposeUIViewController` and Essenty's `ApplicationLifecycle` do not exist on macOS).

## Data flow

UI (`sharedUI`) → `XxxComponent` (Decompose, exposes `Value<Model>`) → `XxxStore` (MVIKotlin) → repository
interface (`core:domain`) → implementation (`core:data`) → DAO (`core:database`). Changes come back as `Flow`s
collected by the store's executor; the component maps `State → Model` in `mapper/`.

## Components and DI

- **Implementations stay `internal` to their module.** Each module contributes them to the app graph from one
  `di/<Module>Bindings` container (`@ContributesTo(AppScope::class) @BindingContainer abstract class`) with
  `@Binds internal abstract val Impl.bindX: Interface` and, for third-party types, `@Provides` in its companion.
  Containers: `CommonBindings`, `DatabaseBindings`, `DataBindings`, and one per feature (`RootBindings`, `HomeBindings`).
- Platform-only inputs (Android `Context`, `SqlDriverFactory`, later HTTP engines) are provided by the platform
  graphs in `sharedLogic` (`AndroidAppGraph`, `NativeAppGraph`).
- Each component is `interface XxxComponent` (with a nested `fun interface Factory`) +
  `internal class DefaultXxxComponent` (`ComponentContext by componentContext`) +
  `internal class DefaultXxxComponentFactory` (`@Inject`), bound in the feature's bindings container.
- A parent receives its children's `Factory` types through its own factory's constructor; Metro wires the chain.
  Nothing calls a `Default…` constructor outside its own module.
- State is exposed as Decompose `Value`; navigation configs are private `@Serializable` types.
- Feature layout: `XComponent`, `DefaultXComponent`, `PreviewXComponent`, `store/XStore` + `store/XStoreFactory`
  (MVIKotlin, created with `instanceKeeper.getStore`), `mapper/XStateMapper` (`State → Model`), `di/XBindings`.
  A module with several components puts each in `component/<name>/` with its own `store/` and `mapper/`.
- Navigation leaves a component through a single `Output` callback in its factory (or `onBack` when that is
  all); the parent maps child outputs to its own. A component with no state of its own has no store.
- The apps get the graph from `createAndroidAppGraph(context)` / `createNativeAppGraph()` and create the root with
  `graph.rootComponentFactory(componentContext)` on the main thread. Android: `retainedComponent` (state saved
  with the Activity). iOS: `AppRoot(ApplicationLifecycle())` with a `StateKeeperDispatcher`; the Swift
  `AppDelegate` calls `saveState` / `restoreState` from `application(_:shouldSave/RestoreSecureApplicationState:)`,
  and the root is created lazily after any restore.

### Adding a feature

1. `feature/<name>/build.gradle.kts` with `alias(libs.plugins.local.feature)`; `include(":feature:<name>")`.
2. Component + store + mapper + `di/<Name>Bindings` as in `feature/home`.
3. A child in `RootComponent` (or the parent that owns it) taking `<Name>Component.Factory`.
4. `<Name>Content` in `shared/ui/.../ui/<name>/` and a branch in the parent's `Children`.
5. Store test in `commonTest` (fake repositories, `DefaultStoreFactory`, `Dispatchers.setMain`).

## Build conventions (`build-logic/convention`)

| Plugin alias | What it does |
|---|---|
| `local.kotlin.multiplatform` | KMP + Android KMP library + serialization; Android + iOS targets; JDK 17; host tests on; coroutines, datetime and serialization in `commonMain`; the `testing` bundle in `commonTest`. Android namespace from the path: `:core:common` → `<app.basePackage>.core.common` |
| `local.compose.multiplatform` | Compose plugin + compiler; runtime, foundation, ui, material3, adaptive, resources, previews |
| `local.feature` | `local.kotlin.multiplatform` + Metro; `api(:core:domain)`, `:core:common`, Decompose and MVIKotlin bundles |
| `local.sqlDelight` | SQLDelight with `generateAsync = true`; Android, native and JDBC (host tests) drivers; `-lsqlite3` for native |

New modules use these plugins. Add every dependency to `gradle/libs.versions.toml` first; never put versions in
build files. Reference modules with typesafe accessors (`implementation(projects.core.domain)`), never
`project(":…")`; the one exception is `build-logic`, where the accessors do not exist.

The base package lives in one place: `app.basePackage` in `gradle.properties` (also the Android applicationId
and the iOS bundle id). `scripts/rename.sh <package> <Name> ["Display Name"]` renames the package and its
directories, every `KmpTemplate` name (Gradle project, Xcode product, `KmpTemplateApplication`) and the display
name; a repository created from the GitHub template runs it on its first push
(`.github/workflows/template-init.yml`, names derived from the repository).

## Stack

Kotlin 2.4 · Compose Multiplatform · Decompose + Essenty · MVIKotlin · Metro DI · kotlinx (coroutines,
serialization, datetime) · SQLDelight · Kermit · Compose resources. Tests: kotlin-test, coroutines-test, Turbine. Releases: fastlane.

## Commands

```bash
./gradlew :androidApp:assembleDebug                       # Android app
./gradlew testAndroidHostTest                             # all JVM unit tests (fast)
./gradlew iosSimulatorArm64Test                           # all iOS simulator tests
./gradlew :sharedUI:compileKotlinIosSimulatorArm64        # iOS compile check
./gradlew :sharedUI:linkDebugFrameworkIosSimulatorArm64   # iOS framework link check
```

iOS: open `app/iosApp/iosApp.xcodeproj` and run; set `TEAM_ID` in `app/iosApp/Configuration/Config.xcconfig`
for a device build.

## CI and releases (`.github/workflows`, `fastlane/`)

- `ci.yml` (pull requests, `main`): Android host tests and a debug APK; iOS simulator tests and a simulator build.
- `release.yml` (tags `v*.*.*`): fastlane builds a signed AAB/APK (Play internal track, draft; APK on the GitHub
  Release) and a signed IPA (TestFlight). Version name = tag, version code = run number. Secrets: `fastlane/README.md`.
- `template-init.yml`: one-time renaming of a repository created from the template.
- Store metadata lives in `fastlane/metadata/{android,ios}/<locale>/`; every upload lane leaves a draft.

## Rules for agents

- Verify a change with the narrowest Gradle task that covers it; run the iOS compile task too when you change
  `commonMain` or `iosMain`.
- Never log, print or persist secrets.
- Do not edit the generated `Res` class or SQLDelight output; change `composeResources/` or `.sq` files.
- Skills for this stack are in `.agents/skills/` (index: `.agents/AGENTS.md`). Use them before writing
  Decompose, MVIKotlin, Metro or Compose code.

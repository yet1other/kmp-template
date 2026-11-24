# KMP Template

Kotlin Multiplatform starter for **Android + iOS** with a shared **Compose Multiplatform** UI.

- Base: the [JetBrains KMP wizard](https://kmp.jetbrains.com/?android=true&ios=true&iosui=compose&includeTests=true)
  (Android, iOS with shared Compose UI, tests).
- Build conventions in `build-logic/convention` (`local.kotlin.multiplatform`, `local.compose.multiplatform`,
  `local.feature`, `local.sqlDelight`).
- `core/` modules: `common`, `domain`, `data`, `database`, `resources`.
- Architecture: Decompose + MVIKotlin components, Metro DI, SQLDelight, Kermit.
- AI agents: `AGENTS.md` (canonical guide), `CLAUDE.md`, `GEMINI.md`, skills in `.agents/skills`
  (`.claude/skills` is a symlink to it).

## Start a new project

**From GitHub ("Use this template").** Name the repository, e.g. `acme/cool-notes`. On its first push
`template-init.yml` renames everything like the KMP wizard does: package `com.acme.coolnotes`, project
`CoolNotes`, app name "Cool Notes", and commits the result. To choose other names, set the repository variables
`APP_PACKAGE`, `APP_NAME`, `APP_DISPLAY_NAME` and run the workflow by hand, or run the script locally.

**Locally.**

```bash
cp -R kmp-template ~/development/MyApp && cd ~/development/MyApp
scripts/rename.sh com.acme.myapp MyApp "My App"
git init && git add -A && git commit -m "Initial commit from kmp-template"
./gradlew testAndroidHostTest :androidApp:assembleDebug
```

The script reads the current names from the project, so it can be run again to rename once more.

Then replace the sample `Note` feature (`core/domain/.../note`, `core/database/.../Note.sq`,
`core/data/.../note`, `feature/home`, `shared/ui/.../ui/home`) with your own.

## Layout

```
app/androidApp      Android entry point (Application owns the graph, Activity the root)
app/iosApp          Xcode project hosting the SharedUI framework
shared/ui           Compose UI for Android and iOS (iOS framework: SharedUI; iosMain: UIKit entry IosApp)
shared/logic        Metro app graph: AppGraph + AndroidAppGraph / NativeAppGraph; AppRoot + StateKeeperUtils (nativeMain)
core/common         dispatchers, logging, Decompose/MVIKotlin helpers, common bindings
core/domain         entities, repository interfaces
core/data           repository implementations
core/database       SQLDelight database, DAOs, platform drivers
core/resources      Compose resources (strings) and AppTheme
feature/root        root component (child stack)
feature/home        sample feature: component + store + mapper + bindings
build-logic         convention plugins
fastlane            store metadata and release lanes (see fastlane/README.md)
scripts/rename.sh   rename package and project
```

## Commands

```bash
./gradlew :androidApp:assembleDebug
./gradlew testAndroidHostTest
./gradlew iosSimulatorArm64Test
```

iOS: open `app/iosApp/iosApp.xcodeproj` in Xcode and run.

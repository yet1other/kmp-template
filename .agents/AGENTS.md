# .agents/AGENTS.md: skills index

AI agent skills for this project. Architecture and build rules are in the root [AGENTS.md](../AGENTS.md).
Claude Code reads these skills through the `.claude/skills` symlink; Codex and Gemini read them here.

## Skill index (`.agents/skills/`)

- **Decompose & MVIKotlin** (`decompose-mvikotlin/`)
  - `decompose-component`: ComponentContext, InstanceKeeper, StateKeeper, lifecycle
  - `decompose-navigation`: ChildStack, ChildSlot, ChildPages, ChildPanels
  - `decompose-compose`: Compose extensions and UI binding
  - `mvikotlin`, `mvikotlin-code`: StoreFactory, CoroutineExecutor, Reducer, Labels (points at `feature/home`)
  - `performance`: Decompose/MVIKotlin performance
- **Compose Multiplatform** (`compose-multiplatform/`): state authoring and hoisting, deferred reads, side
  effects, stability, recomposition performance, slot APIs, modifiers, animations, focus, adaptive layouts, UI tests
- **Kotlin** (`kotlin/`): control flow, coroutines and structured concurrency, Flow state/events, functions,
  value classes
- **Metro DI** (`metro-di/`): `@DependencyGraph`, `@ContributesTo`, `@BindingContainer`, `@Binds`, KMP wiring
- **Android** (`android/`): edge-to-edge, R8 analysis, Play Billing upgrades
- **Apple** (`apple/iphone-duo/`, `ios/`): iPhone Duo layouts and hinge, HIG and Apple design
- **Design** (`design/`): app system, better-* (UI, layout, typography, colors, writing, accessibility), motion, polish
- **App Store Optimization** (`aso/`): metadata, keywords, screenshots, launch, reviews, monetization (start at `aso-router`)
- **Workflow** (`implement-issue/`): take a GitHub/GitLab issue from intake to a finished branch
- **Hindsight** (`hindsight-docs/`): the Hindsight memory server

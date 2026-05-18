# UUKotlinUX

UI and Compose utilities for Silverpine UU Android apps: custom views, ViewModels, drag-and-drop, permissions, preferences-backed state, and activity helpers.

## Maven coordinates

| Artifact | Coordinates |
|----------|-------------|
| UX | `com.silverpine.uu:uu-ux-ktx` |

Published to [Maven Central](https://central.sonatype.com/search?q=com.silverpine.uu) under the `com.silverpine.uu` group.

## What's included

### Classic View system (`com.silverpine.uu.ux`)

- **Activities** — `UUAppCompatActivity`, `UUFragmentActivity`, `UURecyclerActivity`, `UUActivityLauncher`.
- **Custom views** — `UUButton`, `UUImageView`, bordered variants, polygon/circle shapes, `UUPolygonView`.
- **ViewModels** — `UUViewModel`, `UUFragmentViewModel`, `UUAdapterItemViewModel`, `UUViewModelRecyclerAdapter`.
- **Drag and drop** — `UUDragDropViewModel`, shadow builders, touch/long-click listeners.
- **UX helpers** — `UUToast`, `UUAlertDialog`, `UUKeyboard`, `UUAnimation`, `UULayoutTransition`.
- **Events** — `UUEventBus`, `UUEvent`, `UUEventHandler`.
- **Permissions** — `UUPermissions`, `UUPermissionProvider`, `UUPermissionStatus`.
- **Files** — `UUFileProvider`, `UUFiles` for sharing and storage helpers.
- **Media** — `UUTextureViewVideoPlayer`.

### Jetpack Compose (`com.silverpine.uu.compose`)

- **`UUPrefBackedMutable*`** — `MutableState` delegates backed by `SharedPreferences` (primitives, strings, enums, sets).
- **`UUDelegatedMutableState`** — preference delegation patterns for Compose screens.

Built on **`uu-core-ktx`** and AndroidX AppCompat / Compose dependencies.

## Gradle dependency

```kotlin
dependencies {
    implementation("com.silverpine.uu:uu-ux-ktx:<version>")
    implementation("com.silverpine.uu:uu-core-ktx:<version>")
}
```

Enable Compose in the consumer module when using `com.silverpine.uu.compose` APIs.

## Requirements

- AndroidX AppCompat, ConstraintLayout, and Compose BOM versions compatible with your UU `uu_build` catalog
- `uu_min_sdk` / `uu_target_sdk` from UU Gradle properties
- GitHub Packages access for `UUKotlinBuild` version catalog

## Changes in this release

- Preference-backed Compose state helpers for settings and persisted UI state.
- ViewModel-centric drag-and-drop bindings for RecyclerView-driven UIs.
- Permission and file-provider utilities for modern Android storage rules.
- CI unit and managed-device test workflows via UU GitHub Actions.
- Dokka Javadoc JAR published with the release artifact.

---

For prior versions and snapshots, see [GitHub Releases](https://github.com/SilverpineSoftware/UUKotlinUX/releases).

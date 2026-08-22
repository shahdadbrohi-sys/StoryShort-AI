# StoryShort AI

Native offline Android app that turns a short idea into a structured 10-second story concept and saves generated stories locally.

## Run & Operate

- `gradle clean assembleDebug` — build the debug APK
- APK output: `app/build/outputs/apk/debug/app-debug.apk`
- Android SDK is read from `local.properties`; update its path for another machine.

## Stack

- Kotlin, Jetpack Compose, Material 3, Android Gradle Plugin
- Package ID: `com.storyshort.ai`
- minSdk 26, targetSdk 35

## Where things live

- `app/src/main/java/com/storyshort/ai/MainActivity.kt` — launcher
- `app/src/main/java/com/storyshort/ai/ui/` — Compose screens and theme
- `app/src/main/java/com/storyshort/ai/data/` — story model, generator, and local persistence
- `app/src/main/java/com/storyshort/ai/StoryViewModel.kt` — screen state and actions

## Architecture decisions

- Generation is deterministic and fully local; there are no API, login, analytics, ads, or network dependencies.
- History uses a JSON-backed SharedPreferences store to keep the app dependency-light while surviving process/device restarts.

## Product

Users can generate 10-second story concepts from an idea, style, and mood; copy each section or the full concept; regenerate and clear; and manage saved stories in a two-destination history screen.

## User preferences

- The app must remain a real native Android application, not a web app or phone mockup.

## Gotchas

- Gradle requires a Java runtime and a valid Android SDK path in `local.properties`.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details

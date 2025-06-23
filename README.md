# ZwiftViewer (Kotlin Multiplatform)

ZwiftViewer is a Kotlin Multiplatform (KMP) mobile app built to showcase shared business logic, UI patterns, and network integration across Android and iOS. It demonstrates the foundation of a cross-platform companion app experience for a Zwift-like cycling platform.

## Features

- Cross-platform login and session management
- Real-time ride data retrieval from ZwiftPower
- Shared rendering of ride summaries and stats
- Modern, native UI using Jetpack Compose (Android) and SwiftUI (iOS)

## Tech Stack

- Kotlin Multiplatform Mobile (KMM)
- Ktor (HTTP client)
- Kotlinx Serialization
- SwiftUI
- Jetpack Compose
- Gradle (build)
- XCFramework (iOS integration)

## Project Structure

- `shared/` — Shared Kotlin module (models, networking, business logic)
- `composeApp/` — Android app (Jetpack Compose)
- `iosApp/` — iOS app (SwiftUI + integrated shared Kotlin framework)

## Resources

- [Kotlin Multiplatform Docs](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

---

This project was developed as a demonstration of mobile engineering capability for a cross-platform cycling app.

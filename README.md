# 🧭 SuperbAssignment

A Kotlin Multiplatform (KMP) project with a clean architecture approach, using shared business logic and navigation across Android and iOS platforms.

- ✅ **Kotlin Multiplatform**: Shared logic and models
- ✅ **Android Jetpack Compose**: Native UI
- ✅ **iOS SwiftUI**: Native UI
- ✅ **Decompose**: Navigation and lifecycle management (shared)
- ✅ **Koin**: Dependency injection (shared)
- ✅ **Coroutines**
- ✅ **MVI/MVVM + Clean Architecture**: Scalable, testable layers

---

## 🧱 Project Structure

```
superbassignment/
├── shared/                   # Shared KMM logic (Kotlin)
│   ├── presentation/         # Presentation layer (UI logic)
│   │   ├── core/             # Base ViewModel, interfaces
│   │   └── navigation/       # Decompose navigation configs
│   ├── domain/               # Domain models
│   ├── data/                 # Repositories
│   ├── remote/               # Network/data sources (stub API for now)
│   ├── di/                   # Koin modules for dependency injection
├── androidApp/               # Android app (Jetpack Compose)
├── iosApp/                   # iOS app (SwiftUI)
```

---

## 🚀 Getting Started

### ✅ Prerequisites

- Kotlin 2.1.0+
- Android Studio Meerkat Feature Drop | 2024.3.2 or later
- Xcode 16+
- CocoaPods (for iOS integration)

---

## 🔄 Architecture Overview

This project uses **Clean Architecture** with the following layers:

- **Presentation Layer**  
  - `ViewModelComponent<T>` interface (Decompose component Wrapper)  
  - `ViewModel<Args, Intent, ModelState, ViewState, Navigation>` interface (MVI-esque)  
  - Base `ViewModel` class with platform-specific UI bindings
  - Navigation managed with Decompose
- **Domain Layer**  
  - Domain models
- **Data Layer**  
  - Repositories

---

## 🔁 State & Navigation

### MVI State Flow

Each screen defines:
- `ViewState`
- `Intent`
- `Navigation`

Shared `ViewModel`s are exposed to Android and SwiftUI via Decompose ViewModelComponents and then are wrapped in SwiftUI for Swift type safety.

### Navigation

Managed using [Decompose](https://github.com/arkivanov/Decompose) for:
- Back stack navigation
- Lifecycle-aware component management
- Platform-agnostic routing

---

## 🔗 Dependency Injection

- Shared DI modules use [Koin](https://insert-koin.io/)
- Injected into Decompose components via `get()`, with custom factories for things like `CoroutineScope`

---

## 📦 Tech Stack

| Layer             | Technology                    |
|------------------|-------------------------------|
| Shared Logic      | Kotlin Multiplatform, Coroutines |
| Navigation        | Decompose                     |
| UI (Android)      | Jetpack Compose               |
| UI (iOS)          | SwiftUI                       |
| DI                | Koin                          |
| State Management  | MVI / MVVM                    |
| Interop           | Skie, Kotlin/Native           |

---

## 📸 Screenshots

> Add Android/iOS screenshots here

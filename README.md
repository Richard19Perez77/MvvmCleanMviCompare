# MvvmCleanMviCompare

Same task list feature implemented three ways so the architectures can be compared:

- **MVVM** — `mvvm/`
- **MVI** — `mvi/`
- **Clean Architecture** — `clean/` (domain use cases, repository impl, Hilt module)

Each path uses Room plus a fake remote API. ViewModel and repository unit tests live under `app/src/test`.

## Stack

Kotlin, Jetpack Compose, Room, Hilt, JUnit.

## Run

Open the project in Android Studio and run the `app` configuration.

```bash
./gradlew :app:assembleDebug
```

# MvvmCleanMviCompare

Private architecture lab: the **same task list** (add, toggle, delete, sync, load remote) implemented three ways in one app so the differences are visible, not theoretical.

This is not a product app. **AnimeDB** (Clean + MVVM, real API), **FinnStock** (Clean + MVI, real API), and **ArchPatterns** (repository + live WebSocket MVI) are the public proof. This repo is a cheat sheet: identical feature, three folders, switch with three buttons.

## Why it is useful

- Interview: “What is the difference between MVVM and MVI?” — open the app, tap MVVM then MVI, then point at `TaskMVVMViewModel` vs `TaskListContract`.
- Interview: “Where do use cases live?” — `clean/domain/usecase/`. The ViewModel only calls `invoke()`.
- Tests: each path has ViewModel + repository unit tests under `app/src/test/.../mvvm|mvi|clean`.
- Isolation: each path has its **own** Room DB, Hilt module, entity, DAO, and fake API. Switching tabs does not share data. That is deliberate so comparisons stay honest.

Shared UI behavior: Compose list, strikethrough when done, **Sync** / **Load Remote** / **Add Task**. Remote is a `FakeApiService` (`delay(1000)` + two canned tasks), not Retrofit.

## How to run

Open in Android Studio, run `app`. `MainActivity` shows MVVM / MVI / Clean. Default tab is MVVM. `Crossfade` swaps the screen; each tab gets its own `hiltViewModel()`.

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

---

## Shared feature (what “same task list” means)

Every path can:

| Action | Typical call |
| --- | --- |
| Observe list | Room `Flow` of tasks |
| Add | Insert with a timestamp title |
| Toggle done | Update `isDone` |
| Delete | Delete row |
| Sync | Read local list, pretend-upload |
| Load remote | Fake fetch, insert into Room |

The **domain is trivial on purpose**. Complexity is in *where* logic sits, not in the product.

---

## MVVM (`mvvm/`)

**Folders:** `data/` (entity, DAO, DB, repository), `network/` (fake API), `ui/` (screen + ViewModel). Layered by practical Android concerns, driven from the UI.

**Data flow**

```
Compose  →  TaskMVVMViewModel.addTask() / toggleTask() / …
                →  TaskRepository  →  TaskDao / FakeApiService
Compose  ←  viewModel.tasks  (Flow from DAO, exposed with stateIn)
```

`TaskMVVMViewModel` injects the repository. `tasks` is `repository.allTasks.stateIn(WhileSubscribed(5000), emptyList())`. There is **no** single UI-state object: the screen collects the list `Flow` and calls named functions. Loading / error / one-shot messages are not modeled (Sync and Load Remote have no spinner or toast).

**What MVVM means here**

- View: Compose, dumb besides `collectAsState` and click handlers.
- ViewModel: middleman. Public functions *are* the API. Business rules (when to sync, how to toggle) live in the repository or the VM, not in a domain module.
- Model: Room entity `TaskMVVM` used all the way to the UI (no separate domain type).

**Strengths:** little boilerplate, easy to start, VM tests can call `addTask` and assert the repo.  
**Weaknesses:** UI state is inferred from a list `Flow`; one-off events have no channel; VM + repo both hold “app logic.” Fine for a small CRUD screen; messy when the screen has loading, errors, dialogs, and navigation.

**Tests:** `TaskMVVMViewModelTest`, `TaskRepositoryTest`.

---

## MVI (`mvi/`)

**Folders:** same shape as MVVM (`data/`, `network/`, `ui/`) plus an explicit **contract**.

**Contract (`TaskListContract.kt`) — treat the screen as a small state machine**

| Piece | Type | Role |
| --- | --- | --- |
| **Event** | `TaskListEvent` | What happened: `LoadTasks`, `AddTask(title)`, `ToggleTask`, `DeleteTask`, `SyncTasks`, `FetchRemoteTasks` |
| **State** | `TaskListState` | Immutable snapshot: `tasks`, `isLoading` |
| **Effect** | `TaskListEffect` | One-shot: `ShowMessage` → Toast |

**Data flow (unidirectional)**

```
UI  --onEvent(Event)-->  TaskMVIViewModel
                              ├─ repository (same CRUD as MVVM)
                              ├─ _state.value = copy(...)     →  UI collects state
                              └─ _effect.send(ShowMessage)    →  UI LaunchedEffect → Toast
```

The screen never calls `repository` or `addTask()` on the VM. It only `onEvent(...)`. `init` fires `LoadTasks`, which collects `repository.allTasks` into `_state`.

**What MVI means here**

- One reducer-style entry: `onEvent`. Easier to log and test (“given this event, state becomes…”).
- State is a data class, not a loose collection of Flows (though `isLoading` is on the state type and not fully driven for every action).
- Effects use a `Channel` so a Toast is not stuck in `StateFlow` and replayed on rotation the same way.

**Vs MVVM in this repo:** repository code is almost a copy (`TaskRepository` + `FakeApiService`). The difference is **the UI API**: functions vs events, list Flow vs `TaskListState`, no toast vs `TaskListEffect`.

**Strengths:** predictable transitions, testable `onEvent`, a place for one-shots.  
**Weaknesses:** more types; business rules still live in the ViewModel/repository, not in use cases.

**Tests:** `TaskMVIViewModelTest`, `TaskRepositoryTest`.

---

## Clean Architecture (`clean/`)

**Folders:** `domain/`, `data/`, `presentation/`, `di/`. Layered by **abstraction**, driven from the domain.

**Dependency rule**

```
presentation  →  domain  ←  data
     (VM, Compose, TaskUiState)   (Room, FakeApi, TaskRepositoryImpl)
                      ↑
              TaskRepository (interface)
              Task (domain model)
              *UseCase (one action each)
```

Domain has **no** Android, Room, or Hilt types. `Task` is the domain model. Room uses `TaskClean` + `toDomain()` / `toEntity()` in the data layer.

**Use cases** (thin `invoke()` wrappers around the interface):

- `GetAllTasksUseCase`
- `AddTaskUseCase`
- `DeleteTaskUseCase`
- `ToggleTaskCompletionUseCase`
- `SyncTasksUseCase`
- `FetchRemoteTasksUseCase`

`TaskCleanViewModel` injects **use cases**, not the repository. `uiState: StateFlow<TaskUiState>` (`tasks`, `isLoading`). `init` collects `getAllTasks()`. Screen calls `onAddTask`, `onToggleTask`, etc.

Hilt (`clean/di/AppModule.kt`) binds `TaskRepository` → `TaskRepositoryImpl` and provides each use case.

**What Clean means here**

- Swap Room/fake API without changing the VM: only `TaskRepositoryImpl` changes.
- Unit-test a use case with a fake `TaskRepository`; unit-test the VM with fake use cases.
- Cost: more types, mapping, and modules. For a todo list it is verbose on purpose so the *shape* is obvious.

**Vs MVVM/MVI in this repo:** same buttons and Room idea, but the VM is not allowed to talk to DAO/API. Data-access logic is behind the domain interface.

**Tests:** `TaskCleanViewModelTest`, `TaskRepositoryImplTest`.

---

## Side-by-side

| | MVVM | MVI | Clean |
| --- | --- | --- | --- |
| UI talks to | Named VM functions | `onEvent(TaskListEvent)` | `onAddTask` / use-case-backed VM |
| UI state | `Flow<List<Task>>` | `TaskListState` | `TaskUiState` |
| One-shot UI | None | `Channel` + Toast | None in VM (state only) |
| Domain model | Room entity in UI | Room entity in UI | `Task` + mappers |
| Business actions | Repo + VM | Repo + `onEvent` | Use case classes |
| Depends on Android in domain | N/A (no domain module) | N/A | No |
| Best when | Small screen, speed | Complex UI events, UDF | Large app, swap data sources, test use cases |

All three use **Hilt** and **Compose**. None of this is a substitute for a real network stack; the fake API only proves “remote” as a seam.

## Layout

```
app/src/main/java/com/example/mvvmcleanmvicompare/
  MainActivity.kt          # three buttons + Crossfade
  mvvm/  data, network, ui
  mvi/   data, network, ui (contract + VM + screen)
  clean/ data, domain, presentation, di
```

Notes from an earlier pass also live in `.../review.md` (folder comparison). Prefer this README as the source of truth.

## Honest limits

- Package is still `com.example.mvvmcleanmvicompare`.
- Fake network, todo domain, duplicated repository code across MVVM and MVI.
- Clean use cases are one-liners; that is the pattern, not a rich domain.
- `isLoading` exists on MVI/Clean state but is not the focus of every action.

Use this repo to **explain**. Use AnimeDB / FinnStock / ArchPatterns to **show production shape**.

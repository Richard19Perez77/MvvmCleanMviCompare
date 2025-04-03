# Review of mvi mvvm and clean

## Project folder structure

- mvi and mvvm 
  - data
  - network
  - ui

- clean 
  - data 
  - domain
  - presentation

### mvvm and mvi are layered by concern, driven by ui

- practical app concerns

1. data/
   2. includes local models, dao's database
   3. app internal data handling
4. network/ 
   5. retrofit services or fake api's
   6. remote data sources
7. ui/
   8. composables, viewmodels
   9. contracts

#### mvvm

- view model acts as a middleman between ui and data
- no formal separation of business rules from data logic
- good for medium sized apps

#### mvi

- similar folders as MVVM, but the contract (state and events is more strictly defined)
- prioritizes unidirectional data flow
- view model emits state, ui observes and reacts
- clean separation of ui state and event logic
  - but still tightly coupled to ui flow

#### clean

- Layered by abstraction, driven by domain
- Design is with dependency inversion in mind
  - ui depends on domain
  - data depends on domain
  - but domain depends on nothing

1. domain/
   1. pure business logic and use cases
   2. no android or lib. dependencies
   3. highly testable and reusable
2. data/
   1. actual implementations 
      1. room, retrofit, local DB's
   2. implements TaskRepository from domain
   3. can be replaced swapped w/o touching business logic
3.  presentation/
    1.  view model + compose ui
    2.  talks to domain layer via use cases
    3.  holds ui state classes, navigation, etc.

### Summary

- clean vs mvvm and mvi

focus with mvi is domain and business logic
focus with mvvm mvi is ui and data flow

data access logic in clean is in data behind domain abstractions
data access logic in mvvm mvi is in data or inside view model

ui control in mvi mvvm is in ui layer
ui control in clean its in presentation layer

business logic in clean is in domain usecase classes
business logic in mvvm mvi is inside the view model or repository

testability in clean is very high
testability in mvvm mvi is moderate

reusability in clean is high, can reuse domain in other apps
reusability in mvvm mvi is limited outside of Android

dependency direction in clean is UI -> UseCase -> Domain -> Data
dependency direction in mvvm mvi is UI -> ViewModel -> Repository


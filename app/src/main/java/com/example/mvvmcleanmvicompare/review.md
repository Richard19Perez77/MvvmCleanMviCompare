# review of mvi mvvm and clean

## project so far has folder structure that differs

- mvi and mvvm have root 

data
network
ui

- clean has root 

data 
domain
presentation

### mvvm and mvi are layered by concern, driven by ui

practical app concerns

1. data/
   2. includes local models, dao's databased
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

layered by abstraction, driven by domain

with domain data and presentation

design is with dependency inversion in mind

- ui depends on domain
- data depends on domain
- but domain depends on nothing

1. domain/
   2. pure business logic and use cases
   3. no android or lib. dependencies
   4. highly testable and reusable
5. data/
   6. actual implementations 
      7. room, retrofit, local DB's
   8. implements TaskRepository from domain
   9. can be replaced swapped w/o touching business logic
10. presentation/
    11. view model + compose ui
    12. talks to domain layer via use cases
    13. holds ui state classes, navigation, etc.

### summary table

clean vs mvvm/mvi

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


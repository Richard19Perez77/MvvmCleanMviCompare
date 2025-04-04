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

### Summary of differences

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

## testing

1. mvvm
   2. logic separation is lacking with view model doing too much
   3. ui state traceability is problematic because the UI state is often inferred
   4. business logic isolation is poor and can leak into view model
5. mvi
   6. business logic still lives in view model
7. clean
   8. view model, logic separation, and mocking ease is good
   9. ui state traceability, and business logic isolation is good 

### testing coverage potential

1. mvvm 
   2. will lack coverage in ui state flow being manual
   3. will have business rules and use cases in view model, that's problematic
   4. one off effects will need manual testing
5. mvi
   6. testing is easier but business rules in view model make it more difficult
7. clean
   8. less problematic than both

mvvm: easiest to start with, vm testing is fine but logic can blur
mvi: better for ui state predictability and event-driven testing
clean: most modular, best for unit testing use cases, repo's and state separately

unit test examples for one of the view model
use case test setup
or instrumented ui tests to compare behaviour visually


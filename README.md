# Showcase Trivia
This is an Android project showcasing a simple app developed using a selection of the Android Jetpack library suite.

## Overview
The app features a Trivia Quiz of variable difficulty provided through [The Trivia API](https://the-trivia-api.com/). The user is tasked with answering random questions, competing with their highest score of number of questions correctly answered in a row. If a wrong answer is selected, the count ends and the user must start from zero again.

## Technologies used and architecture
### Modules
Albeit too simple to be a perfect example of it, and with some deviations, the project follows the basic clean architecture principles and is modularized in layers by their function.
+ `data` for shared model class declarations as well as data persistence through [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
+ `domain` for business logic, in this case simply REST communication through [Retrofit](https://square.github.io/retrofit/) (this may still be considered data provision and thus belong in the data layer, but was separated for the sake of this example)
+ `ui` for all UI implementation using [Compose](https://developer.android.com/jetpack/androidx/releases/compose) and [Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3)

Traditionally, the UI would be implemented in its own module, but due to the scale of this example, it was merged within the `app` module, which typically serves as the top-level module which puts all the other modules together, but would be nearly empty here otherwise. This module also directs the dependency injection using [Koin](https://insert-koin.io/).

The `common` module is present as layer "alongside" the lowest layers of the application, containing all classes and functions that _may_ be used by any other module, e.g. base MVI classes, extension functions etc.

### MVI Architecture
The project uses the MVI architecture, with the following main components:
+ `Composable` views at the top level
+ `ViewModel` as a manager of their state and communication with other layers
+ `ViewState` as a data class which holds the state of the view model, representing it's current data and how it should be shown in the UI
+ `ViewEvent` as a one-time event which allows the UI to communicate with the view model, typically to allow the user to trigger an action through a screen interaction
+ `ViewCommand` as a one-time event which allows the view model to trigger an action in the UI, e.g. show a dialog

These components interact with each other using Flows and Coroutines with the help of a number of extension functions to simplify their use. Their purpose is to enforce clear layers in the UI and its interactions, allowing for scaling efficiency with acceptable amount of "boilerplate" code. Focus is also given to data immutability, access to fields throughout Composable functions and efficiency in rendering. Thanks to the MVI architecture's separation, composables do not generally interact with their view model directly (as opposed to e.g. the MVVM architecture), thus ensuring they only get updated when truly needed.

### Use Cases
To demonstrate an efficient approach to managing business logic, the Use Case paradigm is used to encapsulate and shield the logic required to download the quiz questions from the REST API.

### Tests
A few very simple unit tests are implemented using the traditional way through [JUnit4](https://developer.android.com/training/testing/local-tests), as well as one instrumentation test. The basic unit tests only test very simple type de/serialization and the instrumented test tests the persistence through Data Store. Neither of these should be considered a good example use of unit testing, but due to the simplicity of the business logic of the application, no better use cases are present.

## Possible improvements
+ Modularization and naming - in a larger application with "real" business logic, modules would typically be split further according the function (or "section") of the application they relate to, as well as the layers of clean architecture. In which case they would also be named according to these functions, rather than simply by the layers they represent. As mentioned, the `ui` and `app` layers would typically be split and a separate module would be introduced for shared resource management, ensuring no library version clashes etc. (which is however currently mitigated by using the library version catalog).
+ UI Design and features - very little attention was given to design, as it is not the goal of this showcase, as well as the amount or UX quality of features. There is a vast room for improvement, but that remains out of scope of this project, whose purpose is to showcase the programmatic side.
+ Use of other contemporary libraries - it would be preferable to be able to show the use of e.g. [Room](https://developer.android.com/training/data-storage/room), but no reasonable enough use case has been found for this project (so far) while trying to keep it simple enough to be easily viewable.
+ Difficulty - to be able to go through a complete demonstration of all situations that may happen within the app, the user must answer at least 5 questions correctly. Due to their randomness and difficulty settings being out of scope (although possible), this can be problematic. Ideally, difficulty scaling (from easy to hard) with the number of successfully answered question would be added in the future.
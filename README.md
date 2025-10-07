# Movie Characters App

A modern, offline-first Android application that displays a list of movie characters, built using Jetpack Compose and adhering to Clean Architecture principles. 
This app showcases best practices in modern Android development, including a reactive MVI pattern, dependency injection with Hilt, 
and comprehensive unit testing. 
The UI replicates a trending liquid glass pattern. A plus is also the animation at the details screen card

List view
https://github.com/user-attachments/assets/64c273e0-1d06-4140-9f85-ef989bed0f2e

Detail view with animations
https://github.com/user-attachments/assets/4fa4e265-2392-4295-94b2-f77fe7ad225c

## Table of Contents

- [Features](#features)
- [Clean Architecture](#architecture)
- [Solid Principles](#architecture)
- [Modularization](#modules)
- [Presentation Layer](#presentation-layer)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Unit Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

-   **Offline-First Support**: Fetches characters from a remote API and caches them in a Room database for offline access.
-   **Dynamic UI**: A sleek, responsive UI built with Jetpack Compose, featuring blurred glassmorphism effects and a switchable background.
-   **Interactive Animations**: The detail card includes a 3D flip on double-tap and a tilt effect on drag.
-   **Robust Error Handling**: Includes a user-friendly "Retry" mechanism for network errors.
-   **Performant Search**: Features a client-side search with debounce logic to ensure a smooth user experience.

## Architecture

This app follows the **Clean Architecture** pattern, which separates concerns into distinct layers. This makes the codebase more scalable, maintainable, 
and testable by promoting a clear separation of business logic from UI and data implementation details.

### Modules

The application is organized into the following modules:

1. **Core Module**: Contains common utilities, base classes, and shared resources used across the app.
2. **Network Module**: Manages network functionalities using Retrofit to fetch character data from the remote API.
3. **Feature Module**: Houses the implementation of specific features. In this project, this includes the character list (moviecharacterlist) and character detail screens. Each feature is self-contained with its own domain, data, and presentation logic.
4. **App Module**: The main application module. It integrates all other modules and handles the primary dependency injection setup with Hilt.

### Presentation Layer

The presentation layer uses **MVVM** (Model-View-ViewModel) combined with **MVI** (Model-View-Intent) principles for better state management:

-   **ViewModel**: Manages UI-related data in a lifecycle-conscious way.
-   **State**: An immutable data class representing the complete state of the UI at any given time (e.g., loading, success with data, or error).
-   **SideEffects**: Side Effect: Used for events that should be consumed only once, such as showing a Toast message or navigating to another screen.
-   **Action**: Represents user intentions (e.g., `GetCharacters`, `SearchCharacters`, `Retry`).

## Technologies Used

-   **Jetpack Compose**: For building native UI.
-   **Coroutines**: For asynchronous programming.
-   **Retrofit**: For network calls.
-   **Dagger/Hilt**: For dependency injection.
-   **Unit Tests**: Ensuring code reliability and correctness.

-   **UI**:
    •Jetpack Compose: For building the native UI declaratively and reactively.
    •Material 3: For modern UI components and design tokens.

-   **Async & Reactive**:
    •Kotlin Coroutines & Flow: For asynchronous programming and building reactive data streams from the UI to the data layer.

-   **Architecture**:
    •Clean Architecture: As the guiding architectural principle.
    •MVVM/MVI: For state management in the presentation layer.

-   **Networking**:
    •Retrofit: For type-safe network calls to the character API.
    •OkHttp: As the underlying HTTP client for Retrofit.

-   **Database**:
    •Room: For persisting character data locally, enabling robust offline support.

-   **Dependency Injection**:
    •Dagger/Hilt: For managing dependencies and decoupling components throughout the app.

-   **Testing**:
    •JUnit4: For running unit tests.
    •MockK: For creating mock objects in tests.
    •kotlinx-coroutines-test: For testing coroutines and ensuring deterministic asynchronous tests.
 
## Future Improvements

If I had more time, here are some features and improvements I would consider adding:

-   **Pagination**: Implement pagination using the Jetpack Paging 3 library to efficiently load the character list, especially if the dataset were much larger. But for this usecase, we have a fixed set of data coming in and the data is noot dynamic. 
-   **UI Testing/Integration tests**: Write end-to-end UI tests using `espresso` or `compose-ui-test-junit4` to verify user flows and screen interactions.
-   **Tablet/Foldable Support**: Create adaptive layouts to provide a better user experience on large screens, such as a master-detail view for the character list and detail screen.
-   **Accessibility Improvements**: Enhance content descriptions and navigation for accessibility services to make the app more usable for all users.
-   **CI/CD Integration**: Set up a continuous integration pipeline (e.g., using GitHub Actions) to automatically build and run tests on every push.
-   **Settings Screen**: Add a settings screen where users could, for example, clear the local cache or toggle between light and dark themes.
-   A Splash Screen and Maybe more modern animations

## Installation

**Important**: This project requires API credentials to fetch data. These are managed securely and are not part of the repository.

1. Clone the repository:

   ```bash
   git clone https://github.com/deonwaju/OpenAssign.git

2.  **Add API Credentials:**
    In the root directory of the project, create a file named `local.properties` or just add the credentials if the file exists. Add your API base URL and authentication token to this file as follows:
    Note on Security: This method prevents secrets from being committed to version control. In a professional production environment, these keys would be stored even more securely, using a service like HashiCorp Vault or managed via GitHub Secrets for CI/CD pipelines.

3. Open the project in Android Studio.

4. Build the project to download all dependencies.4.Run the app on an Android emulator or a physical device (API 24+).

5. Run the app on an Android emulator or a physical device (API 24+).

6. You are all set.

## Unit Testing

This project places a strong emphasis on unit testing to ensure code quality and reliability. Tests are written for:

• ViewModels: To verify that UI state is updated correctly based on results from Use Cases.
• Use Cases (Interactors): To ensure business logic is executed as expected.
• Repositories: To test the data flow logic, such as fetching from the network and caching in the local database.

The testing stack includes JUnit4, MockK for mocking dependencies, and Turbine for testing Flow emissions, 
ensuring that the reactive data streams behave correctly under all conditions.To run the unit tests, execute the following command in the project's root directory:

    ```bash
    ./gradlew test

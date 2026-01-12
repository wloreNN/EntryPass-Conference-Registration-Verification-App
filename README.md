# EntryPass-Conference-Registration-Verification-App
This project is an Android application developed using Kotlin and Room Database for managing conference participant registration and verification.

The application consists of two main modules:

Registration Screen:
Users can register by entering a unique User ID, full name, title (Prof./Dr./Student), and registration type (Full, Student, None). A profile photo is captured using the device camera and stored locally. All data is saved in a Room database following the MVVM architecture.

Verification Screen:
Registered users can be verified by entering their User ID. If the user is found, their information and photo are displayed, and the background color changes according to their registration type. If not found, an error state is shown.

The project demonstrates local data persistence, basic error handling, camera integration, and clean separation of UI and data layers.

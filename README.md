# Smart Pantry Manager

Smart Pantry Manager is a Java Android app that helps users reduce food waste by tracking pantry ingredients and suggesting only recipes that can be cooked using everything already available at home.

## Database choice

This project uses SQLite via Android's built-in SQLiteOpenHelper. SQLite is a good fit for this assignment because it stores data locally on the device, persists across app restarts, and supports the required CRUD operations without needing cloud infrastructure.

## Features

- Add, edit, and delete pantry ingredients
- View pantry items in a RecyclerView
- Seeded recipe catalog with 15 recipes
- Strict recipe matching based on pantry inventory
- Suggested recipe list and recipe detail screen
- Settings screen with unit preference and expiry alerts
- Validation and empty-state messaging

## Setup

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Connect an Android emulator or physical device.
4. Run the app from the toolbar.

## Notes

This project is intentionally built to follow the assignment brief and can be prepared for submission as a ZIP after development is complete.

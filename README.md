## Tuneful

Narrow in on your music tastes and discover new songs using Tuneful. Curate the perfect playlist to fit your current vibe through a quick, interactive interface built with Kotlin and recommendations backed by Gemini AI.

**Features:**
  - Swipe on song previews: right if you like it or left if you aren't interested
  - When you are done, click the next arrow to see a list of songs you liked and preview them again if you wish
    -  _Coming update:_ Directly add this list of songs to your Apple Music or Spotify Library as a playlist!
  - If you are inspired, you can guide the AI agent by adding a song you are in the mood for in the queue
  - Restart your session at any time by pressing the middle refresh button

## Project Screen Shots
<img width="292" height="647" alt="Screenshot 2025-07-29 at 5 21 33 PM" src="https://github.com/user-attachments/assets/d98ae0cc-6e18-4e72-8882-80817b202275" />
<img width="307" height="645" alt="Screenshot 2025-07-29 at 5 22 01 PM" src="https://github.com/user-attachments/assets/a324e469-0887-456a-81f2-1545d6ccfa03" />
<img width="293" height="645" alt="Screenshot 2025-07-29 at 5 22 34 PM" src="https://github.com/user-attachments/assets/b5254f3c-4985-4113-b79f-c8b8c735d09e" />
<img width="295" height="645" alt="Screenshot 2025-07-29 at 5 23 04 PM" src="https://github.com/user-attachments/assets/7295f26d-676c-499b-be7c-6838f8502788" />

## Installation and Setup Instructions

Follow these steps to set up and run the project on your local machine:

### 1️⃣ Prerequisites
- [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or newer
- Android SDK installed (via Android Studio)
- A physical Android device or emulator for testing
- Access to:
  - **Apple Music API** through an [Apple Developer Account](https://developer.apple.com/)
  - **Generative Language API:** generativelanguage.googleapis.com

### 2️⃣ Clone the Repository
```bash
git clone https://github.com/YourUsername/YourProjectName.git
cd Tuneful-Mobile-Public
```

### 3️⃣ Open in Android Studio
  1. Open Android Studio.
  2. Select File > Open... and choose the cloned project folder.
  3. Wait for Gradle sync to complete.
  4. Then open the project in Android Studio to build and launch on an emulator

### 4️⃣ Secrets Folder
This folder contains sensitive API keys and should not be committed. Add your `ApiKeys.kt` file to the Secrets Folder.

Format as follows:
```
object ApiKeys {
  val ANDROID_STUDIO_KEY = "..."
  val SPOTIFY_CLIENT_ID = "..."
  val SPOTIFY_CLIENT_SECRET = "..."
  val RAW_APPLE_DEV_KEY = """
  -----BEGIN PRIVATE KEY-----
  ...
  -----END PRIVATE KEY-----""".trimIndent()
  val APPLE_TEAM_ID = "..."
  val APPLE_KEY_ID = "..."
}
```

### 5️⃣ Build & Run the Project
  1. Click Sync Project with Gradle Files (if prompted).
  2. Connect an Android device (enable Developer Mode & USB Debugging) OR start an emulator from Device Manager.
  3. Press the green Run ▶️ button or Shift + F10.
  4. Select your device/emulator, and the launch the app!



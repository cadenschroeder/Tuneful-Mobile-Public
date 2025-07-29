# Secrets Folder
This folder contains sensitive API keys and should not be committed. Add your `ApiKeys.kt` file here.

Format as follows:

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
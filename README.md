# Cabin Trivia

Free, fully offline aviation trivia for a 1–2 hour flight. Questions ship in the APK. No account, no network.

## What it does

- Loads a bundled catalog (airport codes, airlines, famous flights, meteorology)
- Shows a question and four choices
- Tallies correct vs asked and shows the score at the end

## Toolchain

Same CLI stack as `simple-android-app`: JDK 17, Android SDK, Gradle wrapper. No Android Studio required.

```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export ANDROID_HOME="/opt/homebrew/share/android-commandlinetools"

cd /Users/selmeny/Projects/cabin-trivia
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`

```bash
adb devices
./gradlew :app:installDebug
adb shell am start -n com.cabin.trivia/.MainActivity
```

# Cabin Trivia

Free, fully offline aviation trivia for a 1–2 hour flight. Questions ship in the APK. No account, no network.

## What it does

- Start screen: **Short pack** (20 questions) or **Full catalog** (~90 questions)
- Bundled aviation items: airport codes, airlines, famous flights, meteorology
- Each deal shuffles question order and the four choices (the correct fact still scores)
- Tap a choice to reveal: miss and correct are marked, plus a short explanation; then Continue
- Score is You got X of Y; Play again returns to the start screen
- Dark cabin UI, large tap targets, portrait or landscape
- Rotation restores the same deal; a bad snapshot returns to the start screen
- No account, no network (`INTERNET` is not declared)

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

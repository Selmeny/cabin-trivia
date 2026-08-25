# Cabin Trivia

Free, fully offline aviation trivia for a 1–2 hour flight. Questions ship in the APK. No account, no network.

## What it does

- Loads a bundled catalog of 80–100 questions (airport codes, airlines, famous flights, meteorology)
- Shuffles the full catalog each Play / Play again
- Tap a choice to reveal: the miss and the correct answer are marked, plus a short explanation; then Continue
- Score is shown as You got X of Y
- Rotation restores the same deal (no network)

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

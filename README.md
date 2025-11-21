# MeterMaster - Recreated from chat (v5)

This is a recreated Android Studio project based on the conversation. It provides a prototype app that:
- records meter readings (water/gas/electric),
- captures images with CameraX and runs ML Kit OCR,
- stores readings in Room,
- shows monthly aggregated charts using MPAndroidChart,
- exports readings to CSV and shares via FileProvider,
- supports runtime permissions and DataStore-based theme (dark/light/system).

## Build locally
1. Open this folder in Android Studio (use Open Project).
2. Ensure SDK 34 and JDK 17 are installed.
3. Let Gradle sync and download dependencies.
4. Build > Build Bundle(s) / APK(s) > Build APK(s).
5. APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## CI
Push to a GitHub repo and Actions will build the APK automatically (workflow included at `.github/workflows/build-and-upload-apk.yml`).


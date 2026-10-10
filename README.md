# EduNexus Android WebView app

This Android Studio project creates a standalone Android app with a native multi-section home screen and a button that opens the existing EduNexus login in an Android WebView.

## URLs

- Login: https://edu.sbnexus.in/login
- Login API: https://backend.edu.sbnexus.in/api/auth/login

The existing website, backend, database and authentication are not rebuilt. Internet access is required.

## Build an APK

1. Install Android Studio (current stable) and its Android SDK platform 35.
2. Extract this project ZIP.
3. Open the extracted `edunexus-android-webview` folder in Android Studio.
4. Allow Gradle sync to complete.
5. Use **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
6. Android Studio will show the generated APK location, normally `app/build/outputs/apk/debug/app-debug.apk`.

For a release APK, use **Build > Generate Signed Bundle / APK** and create/choose a signing key. Keep the keystore and passwords private and backed up.

## Current behavior

- App opens to a native EduNexus home screen with feature sections.
- Get started / Sign in opens `https://edu.sbnexus.in/login`.
- A Home button returns to the home screen.
- Web content stays inside WebView only for HTTPS URLs on `edu.sbnexus.in` and its subdomains.
- External HTTP/HTTPS links open in the device browser.
- JavaScript and DOM storage are enabled for modern web login experiences.
- Cleartext HTTP, file access and content access are disabled.
- Android system back navigates WebView history, then returns to home.

## Before production release

Test sign-in, cookie/session persistence, logout, redirects, file uploads/downloads and any popups on real Android devices. If the website depends on third-party identity providers or external payment flows, review those flows and add only explicitly trusted hosts as needed. Never enable universal file access or mixed content as a workaround.

This source project is provided for development. It has not been built or tested against the live EduNexus deployment in this environment.


## Build in GitHub Actions without a local Android SDK

This project includes `.github/workflows/android.yml`.

1. Create a GitHub repository and upload/push the contents of this project to its `main` branch.
2. Open the repository's **Actions** tab.
3. Select **Build EduNexus APK**.
4. Click **Run workflow** (or let it run after pushing changes).
5. When the workflow completes, open its run and download the `edunexus-debug-apk` artifact.
6. Extract the downloaded artifact ZIP to get `app-debug.apk`, then transfer it to your Android phone and install it.

The cloud workflow installs Java and Gradle in the GitHub runner and compiles the APK. It does not require Android SDK installed on your local computer. The generated APK is a debug build intended for initial testing; create a signed release build before public distribution.
# edunexus-android

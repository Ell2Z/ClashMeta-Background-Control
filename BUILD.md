# Build pipeline

The workflow `.github/workflows/auto-build-release.yml` checks the latest stable upstream release daily and publishes a signed build whenever the upstream tag advances.

## Source strategy

This repository intentionally stores only the customization patch, workflow, and upstream version marker. It does not maintain a second copy of the full Android source tree. Each build checks out the exact official upstream release tag and recursive submodules, then applies the patch. This avoids long-lived fork drift and makes the source version of each APK explicit.

## Signing

The upstream Gradle build reads `signing.properties` and `release.keystore` from the source root. The workflow reconstructs those files from the repository Actions secrets `CMFA_KEYSTORE_BASE64` and `CMFA_KEYSTORE_PASSWORD`. The alias is `clashmeta`; the key password equals the store password.

The keystore is intentionally not committed to this public repository. Keep an offline backup. All future release builds must use this exact keystore to remain update-compatible.

## Versioning and release behavior

The workflow accepts stable tags matching `vMAJOR.MINOR.PATCH`, updates the app's `versionName` and computes `versionCode = MAJOR * 100000 + MINOR * 1000 + PATCH`. It only publishes after a successful build and checksum verification. A newer upstream release that breaks the patch or build will fail without publishing an APK.

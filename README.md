# Clash Meta Background Control

A reproducible, automatically updated GitHub Actions build of the official **Clash Meta for Android** with an explicit background broadcast receiver for automation apps such as MacroDroid.

## What this repository maintains

- Tracks the latest stable GitHub Release from [MetaCubeX/ClashMetaForAndroid](https://github.com/MetaCubeX/ClashMetaForAndroid).
- Stores the small receiver patch and build automation here; the complete upstream source and recursive submodules are fetched at build time.
- Adds `com.github.kr328.clash.ExternalControlReceiver`.
- Registers the receiver as an exported component without an intent filter. Automation apps must address it explicitly.
- Builds the official `meta` flavor with package ID `com.github.metacubex.clash.meta`.
- Publishes signed APKs and SHA-256 checksums to this repository's GitHub Releases.

## Automatic updates

The scheduled workflow checks the latest stable upstream Release once per day. When a newer upstream tag is found than the latest release in this repository, it:

1. Updates `UPSTREAM_VERSION` to record the source version being tracked.
2. Checks out that exact upstream tag and its recursive submodules.
3. Applies the receiver patch and aligns the Android version name/code with the upstream tag.
4. Builds all supported APK architectures.
5. Publishes the APKs and `SHA256SUMS.txt` as a GitHub Release using the same tag.

A build failure (including a patch incompatibility after upstream refactoring) stops publication. No broken release is published. You can also start a build manually from **Actions → Auto-build and release Clash Meta with Background Broadcast → Run workflow**.

## Required repository secrets

The release build uses a persistent signing key so future APKs can be installed as updates over earlier builds from this repository.

Configure these two repository Actions secrets under **Settings → Secrets and variables → Actions → New repository secret**:

- `CMFA_KEYSTORE_BASE64`: Base64 text of the private `release.keystore` file.
- `CMFA_KEYSTORE_PASSWORD`: Password protecting the keystore and its `clashmeta` key.

Never commit `release.keystore`, `signing.properties`, or either secret value to this public repository. Keep a secure offline backup of the keystore and password. Losing the keystore means future APKs cannot update installations signed by it.

## Broadcast actions

Explicit target component:

```
com.github.metacubex.clash.meta/com.github.kr328.clash.ExternalControlReceiver
```

Supported actions:

- `com.github.metacubex.clash.meta.action.START_CLASH`
- `com.github.metacubex.clash.meta.action.STOP_CLASH`
- `com.github.metacubex.clash.meta.action.TOGGLE_CLASH`

The first VPN start may still require opening Clash Meta and granting VPN permission once. Android background foreground-service restrictions may also affect starts initiated while the app is backgrounded; verify behavior on the target device.

## Installation and signing migration

The previous v2.11.37 test build used the runner's default Debug signing identity. The new persistent release key is a different signing identity. To move from that test build to this release-signed build, back up your configuration, uninstall the test build, install the first release-signed APK, and grant VPN permission again. Subsequent builds from this repository will use the same key and can update in place.

For a Pixel 9 Pro, use the `arm64-v8a` APK.

## Upstream

- [Official repository](https://github.com/MetaCubeX/ClashMetaForAndroid)
- [Latest upstream Release](https://github.com/MetaCubeX/ClashMetaForAndroid/releases/latest)
- License: upstream project GPL-3.0.

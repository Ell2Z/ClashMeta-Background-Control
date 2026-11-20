# Clash Meta Background Control

A reproducible GitHub Actions build of **Clash Meta for Android v2.11.37** with an additional explicit broadcast receiver for automation apps such as MacroDroid.

## What this repository changes

- Builds from the official `MetaCubeX/ClashMetaForAndroid` tag `v2.11.37`.
- Adds `com.github.kr328.clash.ExternalControlReceiver`.
- Registers the receiver as an exported component without an intent filter. Automation apps should address it explicitly by component.
- Keeps the official Meta flavor application ID: `com.github.metacubex.clash.meta`.
- Produces the `MetaRelease` APKs using GitHub Actions; no local Android build environment is required.

## Build

Every push to the `build/background-broadcast` branch starts the build workflow automatically. Open the run under **Actions** and download the APK artifact after the run succeeds.

The workflow checks out the official source and its submodules, installs the toolchains used by the upstream project, applies the receiver patch, and runs:

```bash
./gradlew --no-daemon app:assembleMetaRelease
```

The APKs and SHA-256 checksums are uploaded as a workflow artifact. Artifacts are retained for 14 days.

## Broadcast actions

Target component:

```
com.github.metacubex.clash.meta/com.github.kr328.clash.ExternalControlReceiver
```

Supported actions:

- `com.github.metacubex.clash.meta.action.START_CLASH`
- `com.github.metacubex.clash.meta.action.STOP_CLASH`
- `com.github.metacubex.clash.meta.action.TOGGLE_CLASH`

Use an explicit broadcast receiver target in MacroDroid. The receiver deliberately has no intent filter, so implicit broadcasts are not supported.

The first VPN start may still require opening Clash Meta and granting its VPN permission once. Android background foreground-service restrictions may also affect starts initiated while the app is backgrounded; the workflow build must be tested on the target device before relying on unattended automation.

## Installation and signing

This workflow currently uses the Android Gradle Plugin's default debug signing configuration because no private release keystore is stored in this repository. **Do not upload a keystore or passwords to this public repository.**

The resulting APK has the same application ID as the official Meta flavor but a different signing certificate. Android therefore will not install it as an in-place update over the official signed APK. Back up any important configuration, uninstall the official app, install the custom APK, and grant VPN permission again.

Keep the generated signing identity consistent for future updates. A future release-signing setup should use a private keystore and GitHub Actions Secrets.

## Upstream

- [MetaCubeX/ClashMetaForAndroid](https://github.com/MetaCubeX/ClashMetaForAndroid)
- [Official v2.11.37 release](https://github.com/MetaCubeX/ClashMetaForAndroid/releases/tag/v2.11.37)

The upstream project is licensed under GPL-3.0. This repository contains a small patch and build automation; the upstream source is fetched during the workflow rather than duplicated here.

# Build pipeline notes

The workflow builds the official Android project in a separate checkout at tag `v2.11.37`. This repository stores only the automation patch and the workflow, so upstream source files are not copied or independently modified here.

The build targets the official `meta` product flavor and `release` build type. The expected application ID is `com.github.metacubex.clash.meta`.

The custom receiver is registered as an exported explicit-only component. It accepts the existing START, STOP and TOGGLE action strings. Android may still restrict foreground-service starts from background broadcasts, so device-side behavior must be verified after the APK is built.

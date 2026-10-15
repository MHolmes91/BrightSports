# System Bars Fork Design

BrightSports will be forked from `gi-os/BrightSports` to `MHolmes91/BrightSports`.
The work will live on a `system-bars` branch and keep ordinary Android content below
the status bar and above the gesture/navigation bar. LightOS-specific score-alert
overlay windows remain unchanged.

The fork will use the upstream release workflow rather than inventing a separate
distribution mechanism. Pushing `system-bars` will build, test, sign, upload, and
publish the release APK. A new private signing key will be stored only in GitHub
Actions secrets. Because Android will not accept a differently signed APK as an
update to the upstream app, the upstream installation must be uninstalled once;
after installing the fork, future fork releases will update in place.

The signing certificate fingerprint checked into the repository will be changed to
the fork's certificate. The keystore and password must never be committed. The
optional report and BrightMarket index secrets are not required for the APK build.

# fastlane

Store metadata and release automation for Google Play and App Store Connect. Credentials live in GitHub
Actions secrets (or your shell for a local run) and are never committed.

Identifiers are read from the project, not repeated here: the Android package from `app.basePackage`
(`gradle.properties`), the iOS bundle id, version and team from `app/iosApp/Configuration/Config.xcconfig`,
the project name from `settings.gradle.kts`.

```bash
bundle install                                        # once (Gemfile: fastlane)
bundle exec fastlane android publish_store_listing_draft
bundle exec fastlane android release                  # APP_VERSION_NAME, APP_VERSION_CODE, KEYSTORE_*, PLAY_STORE_JSON_KEY
bundle exec fastlane ios publish_app_store_listing_draft
bundle exec fastlane ios publish_app_store_release_notes_draft
bundle exec fastlane ios release                      # signed IPA -> TestFlight (not distributed, not submitted)
```

## Lanes

| Lane | What it does |
|---|---|
| `android publish_store_listing_draft` | Uploads `metadata/android` (texts, images, screenshots) with `changes_not_sent_for_review` |
| `android release` | Builds the signed AAB and APK; uploads the AAB as a draft to the internal track with the changelogs |
| `ios publish_app_store_listing_draft` | Uploads `metadata/ios` and `screenshots/` to the editable App Store version (`MARKETING_VERSION`) |
| `ios publish_app_store_release_notes_draft` | Uploads only each locale's `release_notes.txt` |
| `ios release` | Manual Apple Distribution signing, `build_app`, `upload_to_testflight` |

Every store change stays a draft until a human submits it for review.

## Metadata

- Android: `metadata/android/<locale>/{title,short_description,full_description}.txt`,
  `changelogs/default.txt` (Play's "What's New"; the version code is the CI run number, so a per-code file
  would never match).
- iOS: `metadata/ios/<locale>/{name,subtitle,description,keywords,release_notes,support_url,privacy_url}.txt`.
  The release-notes lane fails when a locale has an empty `release_notes.txt`.
- Screenshots: `fastlane/screenshots/<locale>/` (git-ignored; generate or drop them in before uploading).

## Tag-driven releases (`.github/workflows/release.yml`)

`git tag v1.2.3 && git push origin v1.2.3` starts the Android and iOS jobs. The tag gives the version name, the
run number the version code / build number.

Android secrets: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`, `PLAY_STORE_JSON_KEY`.

iOS secrets: `IOS_TEAM_ID`, `IOS_DIST_CERT_P12`, `IOS_DIST_CERT_PASSWORD`, `IOS_PROVISIONING_PROFILE`,
`IOS_PROVISIONING_PROFILE_NAME`, `APP_STORE_CONNECT_KEY_ID`, `APP_STORE_CONNECT_ISSUER_ID`,
`APP_STORE_CONNECT_KEY_CONTENT` (base64 of the .p8).

The certificate must be an Apple Distribution certificate, and the profile must target the bundle id in
`Config.xcconfig`. CI creates a temporary keychain with a random password and removes it, with the profile,
in an `always()` step.

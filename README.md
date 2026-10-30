# AlprKit for Android

On-device license plate recognition (ALPR/ANPR) for Android apps written in Kotlin or Java. AlprKit is the native SDK behind [react-native-alpr](https://github.com/parkr-app/react-native-alpr); one license key works for both.

<p align="center">
  <img src="https://alpr.parkr.app/screenshots/android-scan-nissan-e-nv200.webp" width="200" alt="Android: live scan reading EL 64433">
  <img src="https://alpr.parkr.app/screenshots/android-photo-nissan-leaf.webp" width="200" alt="Android: EL 15211 read from a photo">
</p>
<p align="center"><sub>Photos: Norsk Elbilforening and Mariordo, <a href="https://creativecommons.org/licenses/by/2.0/">CC BY 2.0</a>, via <a href="https://commons.wikimedia.org/wiki/Category:Electric_vehicle_license_plates_of_Norway">Wikimedia Commons</a>, cropped.</sub></p>

- Fully offline, runs on the device (LiteRT from Google Play services)
- Live camera scanning at 30 FPS with GPU acceleration
- Plate text, country and bounding box, several plates per frame, [65 countries](#supported-countries)
- Stable results: reads from several frames are combined into one answer, and boxes move smoothly
- One-shot scanning that waits for a certain read instead of taking the first blurry frame
- Recognition in photos

**The basic scanner is free, also in production.** PRO features need a license key in release builds and work without one in debuggable builds. Get a key at [alpr.parkr.app](https://alpr.parkr.app).

## Platforms

One license key covers your app on every platform.

| Platform | Package | Repository |
| --- | --- | --- |
| Expo and React Native | `react-native-alpr` on npm | [parkr-app/react-native-alpr](https://github.com/parkr-app/react-native-alpr) |
| iOS (Swift) | `AlprKit` through Swift Package Manager | [parkr-app/alpr-ios](https://github.com/parkr-app/alpr-ios) |
| Android (Kotlin) | `app.parkr:alpr` | [parkr-app/alpr-android](https://github.com/parkr-app/alpr-android) |
| Flutter | `flutter_alpr`, in development | [parkr-app/flutter_alpr](https://github.com/parkr-app/flutter_alpr) |
| Capacitor | `capacitor-alpr`, in development | [parkr-app/capacitor-alpr](https://github.com/parkr-app/capacitor-alpr) |

## Supported countries

The model reads plates from 65 countries and reports the country of each plate as `country` and its ISO code as `countryCode`:

🇦🇱 Albania (`AL`), 🇦🇩 Andorra (`AD`), 🇦🇷 Argentina (`AR`), 🇦🇲 Armenia (`AM`), 🇦🇺 Australia (`AU`), 🇦🇹 Austria (`AT`), 🇦🇿 Azerbaijan (`AZ`), 🇧🇭 Bahrain (`BH`), 🇧🇾 Belarus (`BY`), 🇧🇪 Belgium (`BE`), 🇧🇦 Bosnia and Herzegovina (`BA`), 🇧🇷 Brazil (`BR`), 🇧🇬 Bulgaria (`BG`), 🇰🇭 Cambodia (`KH`), 🇨🇦 Canada (`CA`), 🇭🇷 Croatia (`HR`), 🇨🇾 Cyprus (`CY`), 🇨🇿 Czech Republic (`CZ`), 🇩🇰 Denmark (`DK`), 🇪🇪 Estonia (`EE`), 🇫🇮 Finland (`FI`), 🇫🇷 France (`FR`), 🇬🇪 Georgia (`GE`), 🇩🇪 Germany (`DE`), 🇬🇮 Gibraltar (`GI`), 🇬🇷 Greece (`GR`), 🇬🇬 Guernsey (`GG`), 🇭🇺 Hungary (`HU`), 🇮🇸 Iceland (`IS`), 🇮🇩 Indonesia (`ID`), 🇮🇪 Ireland (`IE`), 🇮🇱 Israel (`IL`), 🇮🇹 Italy (`IT`), 🇱🇻 Latvia (`LV`), 🇱🇮 Liechtenstein (`LI`), 🇱🇹 Lithuania (`LT`), 🇱🇺 Luxembourg (`LU`), 🇲🇾 Malaysia (`MY`), 🇲🇹 Malta (`MT`), 🇲🇽 Mexico (`MX`), 🇲🇩 Moldova (`MD`), 🇲🇨 Monaco (`MC`), 🇲🇪 Montenegro (`ME`), 🇳🇱 Netherlands (`NL`), 🇳🇿 New Zealand (`NZ`), 🇲🇰 North Macedonia (`MK`), 🇳🇴 Norway (`NO`), 🇵🇱 Poland (`PL`), 🇵🇹 Portugal (`PT`), 🇶🇦 Qatar (`QA`), 🇷🇴 Romania (`RO`), 🇸🇲 San Marino (`SM`), 🇷🇸 Serbia (`RS`), 🇸🇬 Singapore (`SG`), 🇸🇰 Slovakia (`SK`), 🇸🇮 Slovenia (`SI`), 🇪🇸 Spain (`ES`), 🇸🇪 Sweden (`SE`), 🇨🇭 Switzerland (`CH`), 🇹🇭 Thailand (`TH`), 🇹🇷 Turkey (`TR`), 🇺🇦 Ukraine (`UA`), 🇬🇧 United Kingdom (`GB`), 🇺🇸 United States (`US`), 🇻🇳 Vietnam (`VN`).

## Installation

```kotlin
repositories {
  maven("https://alpr.parkr.app/maven") { content { includeGroup("app.parkr") } }
}

dependencies {
  implementation("app.parkr:alpr:0.4.0")
}
```

Requires Android 7.0 (API 24) and Google Play services, which provides the LiteRT runtime. Add your key to `AndroidManifest.xml`:

```xml
<meta-data android:name="app.parkr.alpr.LICENSE_KEY" android:value="eyJ2Ijox..." />
```

## Example app

[`example/`](example) is a minimal Kotlin app: a full-screen camera view that shows the plates it reads. Open it in Android Studio, or run `./gradlew :app:installDebug` in `example/` with a phone connected.

## Live camera

```kotlin
val camera = AlprCameraView(context).apply {
  scansPlates = false
  listener = object : AlprCameraViewListener {
    override fun onPlate(view: AlprCameraView, plate: AlprPlate) {
      Log.i("alpr", "Read ${plate.plate} ${plate.countryCode}")
    }
    override fun onError(view: AlprCameraView, error: AlprError) {
      Log.w("alpr", "${error.code.value}: ${error.message}")
    }
  }
}
container.addView(camera)
camera.isActive = true
```

The view follows the activity lifecycle by itself. Request the `CAMERA` permission before setting `isActive`.

Listener methods:

- `onPlate`: each plate once, when several frames agree. For continuous scanning. PRO
- `onScan`: one plate per activation, once the reads are certain. For scanning a single plate. PRO
- `onPlates`: confirmed plates in view with consensus text and smoothed boxes, throttled
- `onPlatesRaw`: every read in the current frame, unsmoothed, throttled
- `onError`

Set `recognizesPlates` or `scansPlates` to `false` when you don't implement that method. Turning off `showOverlay` or `showScanRegion` is PRO.

## Photos (PRO)

```kotlin
val plates = AlprEngine.scanImage(context, uri.toString(), AlprConfig(ocrThreshold = 0.5f))
```

## Telemetry

```kotlin
Alpr.setTelemetryEnabled(false)
```

Runtime telemetry sends at most one anonymous report per day per device. It is on by default without a license and off with one; set the `app.parkr.alpr.TELEMETRY_ENABLED` meta-data to change the default.

AlprKit is licensed under the Parkr ALPR SDK License Agreement (https://alpr.parkr.app/license).

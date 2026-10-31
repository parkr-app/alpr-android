plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "app.parkr.alpr.example"
  compileSdk = 36

  defaultConfig {
    applicationId = "app.parkr.alpr.example"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  implementation("app.parkr:alpr:0.4.1")
  implementation("androidx.activity:activity-ktx:1.10.1")
}

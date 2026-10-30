pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven("https://alpr.parkr.app/maven") { content { includeGroup("app.parkr") } }
  }
}

rootProject.name = "alpr-android-example"
include(":app")

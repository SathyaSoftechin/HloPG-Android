buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Hilt Gradle Plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.57.2")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Hilt plugin (deferred for module use)
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}

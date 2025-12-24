
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Hilt
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

// ✅ Force JDK 17 toolchain
kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.hlopg"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hlopg"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // ✅ JDK 17 compatible
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

// ✅ Kapt configuration for Hilt
kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {

    /* ---------------- Core ---------------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    /* ---------------- Lifecycle ---------------- */
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    /* ---------------- Networking ---------------- */
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.12")

    /* ---------------- Compose (BOM) ---------------- */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    /* ---------------- Coil ---------------- */
    implementation("io.coil-kt:coil-compose:2.4.0")

    /* ---------------- DataStore & Security ---------------- */
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    /* ---------------- Play Services ---------------- */
    implementation(libs.play.services.places)

    /* ---------------- Hilt ---------------- */
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.foundation.foundation2)
    implementation(libs.androidx.compose.animation.core)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    /* ---------------- Unit Tests ---------------- */
    testImplementation(libs.junit)

    /* ---------------- Android Tests ---------------- */
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    /* ---------------- Debug ---------------- */
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
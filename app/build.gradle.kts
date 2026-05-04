plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.personalexpensestracker"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.personalexpensestracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // ── Navigation ────────────────────────────────────────────
    implementation(libs.androidx.navigation.compose)

    // ── ViewModel ─────────────────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ── Room ──────────────────────────────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)          // Flow + coroutines support
    ksp(libs.androidx.room.compiler)                // code generation

    // ── Retrofit ──────────────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)    // JSON parsing
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor) // log API calls in debug

    // ── WorkManager ───────────────────────────────────────────
    implementation(libs.androidx.work.runtime.ktx)  // coroutine support

    // ── DataStore ─────────────────────────────────────────────
    implementation(libs.androidx.datastore.preferences)

    // ── Gson ──────────────────────────────────────────────────
    implementation(libs.gson)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Hilt with WorkManager
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Hilt with Compose
    implementation(libs.androidx.hilt.navigation.compose)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Coroutines testing — for viewModelScope and Flow
    testImplementation(libs.kotlinx.coroutines.test)

// Turbine — for testing Flows cleanly
    testImplementation(libs.turbine)

// MockK — Kotlin-friendly mocking (Step 4, but add it now)
    testImplementation(libs.mockk)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
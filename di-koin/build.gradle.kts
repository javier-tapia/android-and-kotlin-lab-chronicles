plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.android_and_kotlin_lab_chronicles.di_koin"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Koin core no requiere plugins adicionales acá, usará el del módulo principal
}
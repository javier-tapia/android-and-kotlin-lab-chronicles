plugins {
    alias(libs.plugins.android.library)
}

android {
    // Namespace único para identificar este componente binario de forma aislada
    namespace = "com.example.android_and_kotlin_lab_chronicles.di_manual"
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
    // Inicialmente vacío, listo para las clases de inyección a mano
}
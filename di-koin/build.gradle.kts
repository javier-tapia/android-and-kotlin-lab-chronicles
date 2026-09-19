plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.android_and_kotlin_lab_chronicles.di_koin"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Koin opera en runtime mediante DSL; no requiere procesadores de anotaciones (KSP/kapt)
    // ni plugins adicionales acá.
    // Se usa 'api' en lugar de 'implementation' para que cuando ':app' consuma a ':di-koin',
    // herede automáticamente los símbolos de Koin sin necesidad de duplicar las dependencias en
    // el 'build.gradle.kts' de la app.
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.androidx.compose)
}

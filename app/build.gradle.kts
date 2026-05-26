plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.android_and_kotlin_lab_chronicles"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.android_and_kotlin_lab_chronicles"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // NDK significa Android Native Development Kit (Kit de Desarrollo Nativo de Android).
        // En el ecosistema de Android, normalmente programas en Kotlin o Java, y ese código corre
        // arriba de una máquina virtual (ART - Android Runtime). Sin embargo, hay herramientas
        // pesadas —como FFmpeg (procesamiento de video), motores de juegos en Unity/Unreal o
        // librerías de criptografía— que están escritas directamente en lenguajes nativos de bajo
        // nivel como C o C++.
        // El NDK es el puente que le permite a Android compilar, empaquetar y comunicar ese código
        // en C/C++ con la app de Kotlin.
        // El bloque 'ndk{}' permite órdenes directas al compilador sobre cómo manejar esos archivos
        // binarios nativos (que tienen extensión '.so').
        // FFmpeg-Kit requiere especificar qué arquitecturas compilar para no incluir archivos innecesarios.
        // La arm64-v8a es para el teléfono físico Moto G60s, y mantiene x86_64 para el emulador
        ndk {
            abiFilters.addAll(listOf("x86_64", "arm64-v8a"))
        }
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        compose = true
        // Necesario para algunas versiones modernas de FFmpeg
//        prefab = true
    }

    packaging {
        resources {
            // Esto es obligatorio cuando se usan librerías nativas potentes como FFmpeg y WebRTC juntas.
            pickFirsts += "**/libc++_shared.so"
        }
    }
}

dependencies {
    // Vinculación de los módulos del lab de DI
    implementation(project(":di-manual"))
    implementation(project(":di-koin"))
    implementation(project(":di-hilt"))

    // El Compose BOM (Bill of Materials) solo gestiona las versiones de las librerías que son parte
    // del Core de Compose (UI, Foundation, Runtime, Material, Animation, etc.).
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Nav3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // Multimedia: Media3/ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Multimedia: CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Multimedia: Streaming Adaptativo
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.exoplayer.dash)

    // Multimedia: Real-time communication
    implementation(libs.webrtc.android)

    // Multimedia: Processing
    implementation(libs.google.mlkit.face.detection)
    implementation(libs.google.mlkit.barcode.scanning)
    implementation(libs.ffmpeg.android)

    // OpenCV
//    OpenCV es un caso especial porque no se puede instalar solo con una línea de código.
//    Para usarlo, el procedimiento es:
//    1. Descargar el SDK de Android desde la web oficial de OpenCV.
//    2. Importarlo en Android Studio como un Module (File > New > Import Module).
//    3. Vincular ese módulo a la app.
//    implementation(libs.opencv.android)

    // Network and Serialization
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.ktor.serialization.kotlinx)

    // Sentry and Segment
    implementation(libs.sentry.android)
    implementation(libs.segment.analytics)

    // Coil
    implementation(libs.coil.compose)

    // DI con Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    // KSP (Kotlin Symbol Processing) es un procesador de anotaciones en tiempo de compilación que
    // ofrece mejor rendimiento y tiempos de compilación más rápidos en comparación con KAPT
    // (Kotlin Annotation Processing Tool)
    ksp(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
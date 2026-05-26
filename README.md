<h1>🧪 Android & Kotlin Lab Chronicles</h1>

> *"The only truly honest source of information in the world is the code."*
>
> Robert C. "Uncle Bob" Martin

Este proyecto es un **laboratorio técnico personal** diseñado para registrar, experimentar y documentar el aprendizaje continuo en el ecosistema de Android y el lenguaje Kotlin.

Cada paquete representa un experimento enfocado en resolver problemas específicos, implementar patrones de diseño o probar nuevas librerías.

***Index***:
<!-- TOC -->
  * [🏛️ Estructura del Laboratorio](#-estructura-del-laboratorio)
    * [🤖 Android Fundamentals](#-android-fundamentals)
    * [💉 Inyección de Dependencias](#-inyección-de-dependencias)
    * [🗺️ Geolocation](#-geolocation)
    * [🛠️ Integrations (SDKs)](#-integrations-sdks)
    * [🎨 Jetpack Compose](#-jetpack-compose)
    * [🧩 Kotlin Pure](#-kotlin-pure)
    * [🎥 Multimedia & Streaming](#-multimedia--streaming)
    * [📡 Networking & Monitoring](#-networking--monitoring)
  * [🛠️ Arquitectura del Proyecto](#-arquitectura-del-proyecto)
<!-- TOC -->

---

## 🏛️ Estructura del Laboratorio

### 🤖 Android Fundamentals
Fundamentos del framework y componentes principales del sistema.
*   **Navigation & Activity Flow**:
    *   **ActivityResultContracts**
    *   **Back Press Handling**
    *   **Deep Links & App Links**
*   **Fragments**: Gestión de ciclo de vida, transacciones y comunicación.
*   **OS Integrations**:
    *   **Background**: WorkManager, Services, BroadcastReceiver, Alarms.
    *   **System**: Notificaciones, Widgets, Shortcuts, In-App Updates y In-App Reviews.
    *   **Lifecycle**: Manejo de *Process Death*.
*   **Data & Sharing**:
    *   **Persistence**: Room, Realm, SharedPreferences, EncryptedSharedPreferences, DataStore, gestión de archivos (*Scoped Storage*).
    *   **Sharing**: Intents (implícitos/explícitos), IPC, FileProvider, MediaStore, ContentProviders.

### 💉 Inyección de Dependencias
Configuración y uso de Inyección de Dependencias.
*   **Hilt**: DI con Hilt
*   **Koin**: DI con Koin
*   **Manual**: DI manual (sin frameworks)

### 🗺️ Geolocation
Fundamentos sobre Geolocalización y Mapas.

### 🛠️ Integrations (SDKs)
Integración con SDKs de terceros.
*   **Firebase**: Auth, FCM (Firebase Cloud Messaging), Remote Config e inicialización forzada.
*   **Auth & Identity**: Implementación de Google OAuth + PKCE.

### 🎨 Jetpack Compose
Catálogo de componentes modernos y gestión de estados.
*   **Catalog**: Librería de componentes personalizados.
*   **Animations**: Transiciones complejas y estados animados.
*   **Side Effects**: Uso correcto de `LaunchedEffect`, `derivedStateOf`, `DisposableEffect`, etc.
*   **Navigation**: Implementación de las últimas estrategias de navegación declarativa.

### 🧩 Kotlin Pure
Lógica de lenguaje puro y patrones de diseño fuera del contexto directo de la UI.
*   **Design Patterns**: Patrones de diseño (Singleton, Builder, etc.) con tres variantes de implementación y uso en Kotlin: **clásica** (estructural), **idiomática** (uso de lambdas en lugar de subclases) y **DSL-style** (más declarativa y expresiva).
*   **Async & Concurrency**: Corrutinas, Flows (incluyendo ``StateFlow`` y ``SharedFlow``), anotaciones y funciones relacionadas con la asincronía y el control de concurrencia (``Mutex``, ``@Volatile``, etc.).
*   **Collections**: Operaciones agregadas, transformaciones y filtrado avanzado.
*   **Data Ops**: Operaciones con JSON, manipulación de Strings (incluye Spans), Serialización y Regex.
*   **Result Handling**: Estrategias para la gestión de resultados y errores funcionales mediante el uso de Either (Left/Right) y la clase Result nativa de Kotlin.

### 🎥 Multimedia & Streaming
Reproducción y grabación de audio y video.
*   **Video/Audio**: ExoPlayer, JW Player, Bitmovin, CameraX, MediaRecorder, MediaPlayer, AudioRecord, AudioTrack.
*   **Processing**: FFmpeg, ML Kit, OpenCV.
*   **Streaming**: WebRTC, RTMP, HLS, DASH, estrategias de Caching.

### 📡 Networking & Monitoring
Consumo de APIs y monitoreo.
*   **Network**: Comparativa entre Retrofit y Ktor Client.
*   **Monitoring**: Integración de Sentry y Segment para trazabilidad de errores.

---

## 🛠️ Arquitectura del Proyecto

Para mantener la consistencia en todos los experimentos, el proyecto utiliza un sistema de *wrappers* en el paquete `core`:

- **`BaseRawScreen`**: Proporciona el `Theme` y el `Scaffold` básico. Permite control total sobre los *Insets* del sistema (_Edge-to-Edge_).
- **`BaseLayoutScreen`**: Extiende la base añadiendo una estructura de `Column` con márgenes estandarizados (``16.dp``) y soporte para títulos.
- **`SamplesShowcase`**: Contenedor utilitario diseñado para exhibir múltiples experimentos o variantes de componentes de forma organizada y estandarizada. Utiliza un patrón de ***Slot* API**.

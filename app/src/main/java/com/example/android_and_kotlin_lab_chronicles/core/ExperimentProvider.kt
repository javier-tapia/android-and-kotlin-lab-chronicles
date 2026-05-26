package com.example.android_and_kotlin_lab_chronicles.core

import com.example.android_and_kotlin_lab_chronicles.core.models.Experiment
import com.example.android_and_kotlin_lab_chronicles.core.models.ExperimentTopic
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.DatabasesActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.file_storage.FileStorageActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.preferences.PreferencesActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.content_providers.ContentProvidersActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.file_sharing.FileSharingActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.intents.IntentsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.ipc.IpcActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.media_store.MediaStoreActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.fragments.FragmentsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.activity_result_contracts.ActivityResultFirstActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.back_pressed_handling.BackPressedActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.deeplinks_and_applinks.DeepLinksActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.BackgroundActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.lifecycle.LifecycleActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.system.SystemActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.hilt.HiltLabActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin.KoinLabActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.manual.ManualLabActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.geolocation.GeolocationActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.integrations.firebase.FirebaseActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.integrations.auth_and_identity.AuthActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.animations.AnimationsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.CatalogActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation.NavigationActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects.SideEffectsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.async_and_concurrency.AsyncActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.collections.CollectionsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops.DataOpsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.design_patterns.DesignPatternsActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.result_handling.ResultHandlingActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.audio_and_video.AudioVideoActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.audio_only.AudioOnlyActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.processing.ProcessingActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.streaming.StreamingActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.monitoring.MonitoringActivity
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.NetworkActivity
import kotlin.jvm.java

object ExperimentProvider {
    val experiments = listOf(
        Experiment(
            title = "Databases (Room & Realm)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = DatabasesActivity::class.java,
        ),
        Experiment(
            title = "File Storage (Scoped & Internal)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = FileStorageActivity::class.java,
        ),
        Experiment(
            title = "Preferences (SharedPrefs & DataStore)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = PreferencesActivity::class.java,
        ),
        Experiment(
            title = "Content Providers",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = ContentProvidersActivity::class.java,
        ),
        Experiment(
            title = "FileProvider (Sharing)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = FileSharingActivity::class.java,
        ),
        Experiment(
            title = "Intents (Implicit & Explicit)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = IntentsActivity::class.java,
        ),
        Experiment(
            title = "IPC (Inter-Process Communication)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = IpcActivity::class.java,
        ),
        Experiment(
            title = "Media Store (Gallery Access)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = MediaStoreActivity::class.java,
        ),
        Experiment(
            title = "Fragments (Handling & Transactions)",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = FragmentsActivity::class.java,
        ),
        Experiment(
            title = "ActivityResultContracts",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = ActivityResultFirstActivity::class.java,
        ),
        Experiment(
            title = "Back Pressed Handling",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = BackPressedActivity::class.java,
        ),
        Experiment(
            title = "Deep Links & App Links",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = DeepLinksActivity::class.java,
        ),
        Experiment(
            title = "Background & System Receivers",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = BackgroundActivity::class.java,
        ),
        Experiment(
            title = "Lifecycle & Process Death",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = LifecycleActivity::class.java,
        ),
        Experiment(
            title = "System Integrations",
            topic = ExperimentTopic.FUNDAMENTALS,
            target = SystemActivity::class.java,
        ),
        Experiment(
            title = "DI con Hilt",
            topic = ExperimentTopic.DI,
            target = HiltLabActivity::class.java,
        ),
        Experiment(
            title = "DI con Koin",
            topic = ExperimentTopic.DI,
            target = KoinLabActivity::class.java,
        ),
        Experiment(
            title = "DI Manual",
            topic = ExperimentTopic.DI,
            target = ManualLabActivity::class.java,
        ),
        Experiment(
            title = "Geolocation & Maps",
            topic = ExperimentTopic.GEOLOCATION,
            target = GeolocationActivity::class.java,
        ),
        Experiment(
            title = "Firebase SDKs",
            topic = ExperimentTopic.INTEGRATIONS,
            target = FirebaseActivity::class.java,
        ),
        Experiment(
            title = "Auth & Identity (Google OAuth & PKCE)",
            topic = ExperimentTopic.INTEGRATIONS,
            target = AuthActivity::class.java,
        ),
        Experiment(
            title = "Compose Animations",
            topic = ExperimentTopic.COMPOSE,
            target = AnimationsActivity::class.java,
        ),
        Experiment(
            title = "Compose Component Catalog",
            topic = ExperimentTopic.COMPOSE,
            target = CatalogActivity::class.java,
        ),
        Experiment(
            title = "Compose Navigation (Nav3 & Clásico)",
            topic = ExperimentTopic.COMPOSE,
            target = NavigationActivity::class.java,
        ),
        Experiment(
            title = "Compose Side Effects",
            topic = ExperimentTopic.COMPOSE,
            target = SideEffectsActivity::class.java,
        ),
        Experiment(
            title = "Async & Concurrency (Coroutines & Flows)",
            topic = ExperimentTopic.KOTLIN,
            target = AsyncActivity::class.java,
        ),
        Experiment(
            title = "Kotlin Collections",
            topic = ExperimentTopic.KOTLIN,
            target = CollectionsActivity::class.java,
        ),
        Experiment(
            title = "Data Operations (JSON, Regex, Strings)",
            topic = ExperimentTopic.KOTLIN,
            target = DataOpsActivity::class.java,
        ),
        Experiment(
            title = "Design Patterns (3 Variants)",
            topic = ExperimentTopic.KOTLIN,
            target = DesignPatternsActivity::class.java,
        ),
        Experiment(
            title = "Result Handling (Either & Result)",
            topic = ExperimentTopic.KOTLIN,
            target = ResultHandlingActivity::class.java,
        ),
        Experiment(
            title = "Audio & Video (ExoPlayer & CameraX)",
            topic = ExperimentTopic.MULTIMEDIA,
            target = AudioVideoActivity::class.java,
        ),
        Experiment(
            title = "Audio Only (MediaPlayer & AudioRecord)",
            topic = ExperimentTopic.MULTIMEDIA,
            target = AudioOnlyActivity::class.java,
        ),
        Experiment(
            title = "Media Processing (FFmpeg & ML Kit)",
            topic = ExperimentTopic.MULTIMEDIA,
            target = ProcessingActivity::class.java,
        ),
        Experiment(
            title = "Media Streaming (HLS, DASH, WebRTC)",
            topic = ExperimentTopic.MULTIMEDIA,
            target = StreamingActivity::class.java,
        ),
        Experiment(
            title = "Networking (Retrofit vs Ktor)",
            topic = ExperimentTopic.NETWORKING,
            target = NetworkActivity::class.java,
        ),
        Experiment(
            title = "Monitoring (Sentry & Segment)",
            topic = ExperimentTopic.NETWORKING,
            target = MonitoringActivity::class.java,
        ),
    )
}

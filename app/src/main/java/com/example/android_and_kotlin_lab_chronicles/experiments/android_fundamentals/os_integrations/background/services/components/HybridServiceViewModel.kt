package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.MusicPlayerHybridService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * @see ForegroundServiceViewModel
 * @see BoundServiceViewModel
 */
@HiltViewModel
class HybridServiceViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0)
    val playbackProgress: StateFlow<Int> = _playbackProgress.asStateFlow()

    private var serviceRef: WeakReference<MusicPlayerHybridService>? = null
    private var observeJob: Job? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? MusicPlayerHybridService.LocalBinder
            val boundService = binder?.getService()

            serviceRef = boundService?.let { WeakReference(it) }

            boundService?.let { service ->
                observeJob?.cancel()
                observeJob = viewModelScope.launch {
                    coroutineScope {
                        launch { service.isPlaying.collect { _isPlaying.value = it } }
                        launch { service.playbackProgress.collect { _playbackProgress.value = it } }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            observeJob?.cancel()
            serviceRef = null
            _isPlaying.value = false
        }
    }

    fun bindService() {
        if (serviceRef?.get() == null) {
            val intent = Intent(application, MusicPlayerHybridService::class.java)
            application.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    fun unbindService() {
        if (serviceRef?.get() != null) {
            observeJob?.cancel()
            try {
                application.unbindService(serviceConnection)
            } catch (e: IllegalArgumentException) {
                log("FileDownloadBoundViewModel", "Error", e)
            }
            _isPlaying.value = false
            serviceRef = null
        }
    }

    fun startAndPlay() {
        MusicPlayerHybridService.start(application)
        bindService()
    }

    fun pause() {
        MusicPlayerHybridService.pause(application)
    }

    fun stopServiceCompletely() {
        unbindService()
        MusicPlayerHybridService.stop(application)
        _playbackProgress.value = 0
    }

    override fun onCleared() {
        unbindService()
    }
}
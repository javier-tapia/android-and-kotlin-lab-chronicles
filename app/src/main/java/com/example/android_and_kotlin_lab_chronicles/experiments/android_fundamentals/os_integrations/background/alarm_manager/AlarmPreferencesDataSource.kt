package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Objeto de transferencia de datos persistente serializable para representar una alarma
 * guardada en la capa local de almacenamiento ligero.
 *
 * @property id Identificador único de la alarma.
 * @property title Título descriptivo de la alarma.
 * @property message Mensaje secundario o cuerpo de la notificación.
 * @property triggerTime Marca de tiempo exacta en milisegundos (`Epoch Millis`) en la que debe dispararse.
 */
@Serializable
data class AlarmEntityDto(
    val id: Int,
    val title: String,
    val message: String,
    val triggerTime: Long
)

/**
 * Fuente de datos orientada a la persistencia local de alarmas utilizando [SharedPreferences]
 * en combinación con [kotlinx.serialization] para el mapeo estructurado de objetos JSON.
 *
 * Simula un entorno productivo sin la fricción de configurar entidades, DAOs y migraciones de Room.
 *
 * @property context Contexto de aplicación utilizado para inicializar el contenedor de preferencias.
 */
class AlarmPreferencesDataSource(val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Instancia configurada de Json tolerante a cambios menores de esquema
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    /**
     * Almacena o actualiza los datos de una alarma activa en el almacenamiento local.
     *
     * @param payload Objeto de negocio con los datos principales de la alarma.
     * @param triggerTime Marca de tiempo exacta del disparo de la alarma en milisegundos.
     */
    fun saveAlarm(payload: AlarmPayload, triggerTime: Long) {
        val entity = AlarmEntityDto(
            id = payload.id,
            title = payload.title,
            message = payload.message,
            triggerTime = triggerTime
        )
        val serializedValue = json.encodeToString(entity)
        prefs.edit { putString("$KEY_PREFIX${payload.id}", serializedValue) }
    }

    /**
     * Remueve una alarma de la persistencia local según su identificador único.
     *
     * @param id Identificador de la alarma a eliminar.
     */
    fun removeAlarm(id: Int) {
        prefs.edit { remove("$KEY_PREFIX$id") }
    }

    /**
     * Recupera todas las alarmas persistidas actualmente en el almacenamiento local.
     *
     * @return Mapa asociativo indexado por ID de alarma con su respectivo DTO.
     */
    fun getAllAlarms(): Map<Int, AlarmEntityDto> {
        val result = mutableMapOf<Int, AlarmEntityDto>()
        val allEntries = prefs.all

        for ((key, value) in allEntries) {
            if (key.startsWith(KEY_PREFIX) && value is String) {
                val idStr = key.removePrefix(KEY_PREFIX)
                val id = idStr.toIntOrNull() ?: continue

                try {
                    val entity = json.decodeFromString<AlarmEntityDto>(value)
                    result[id] = entity
                } catch (e: Exception) {
                    // Mitigación defensiva ante corrupción de datos locales en JSON
                    log(
                        "AlarmPreferencesDataSource",
                        "Corrupción de datos locales en JSON",
                        e
                    )
                    removeAlarm(id)
                }
            }
        }
        return result
    }

    companion object {
        private const val PREFS_NAME = "alarm_lab_prefs"
        private const val KEY_PREFIX = "alarm_item_"
    }
}

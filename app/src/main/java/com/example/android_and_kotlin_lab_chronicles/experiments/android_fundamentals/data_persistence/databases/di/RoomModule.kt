package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.di

import android.content.Context
import androidx.room.Room
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.dao.RoomUserDao
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.RoomLabDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Inyección de Dependencias de Hilt encargado de proveer las instancias
 * de la base de datos relacional SQLite ([RoomLabDatabase]) y sus *Data Access Objects* (DAOs).
 *
 * Se instala en [SingletonComponent] para garantizar que las conexiones físicas
 * a SQLite perduren durante todo el ciclo de vida del proceso de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "lab_room_db"

    /**
     * Provee la instancia única (*Singleton*) de [RoomLabDatabase].
     *
     * @param context Contexto global de la aplicación inyectado mediante ``@ApplicationContext``.
     * @return Instancia singleton construida mediante [Room.databaseBuilder].
     */
    @Provides
    @Singleton
    fun provideRoomLabDatabase(
        @ApplicationContext context: Context
    ): RoomLabDatabase {
        return Room.databaseBuilder(
            context,
            RoomLabDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(RoomLabDatabase.MIGRATION_1_2)
            .build()
    }

    /**
     * Provee la instancia de [RoomUserDao] requerida para las operaciones de persistencia.
     *
     * Room genera automáticamente la implementación del DAO durante la compilación mediante KSP.
     *
     * @param database Instancia singleton de [RoomLabDatabase] inyectada por Hilt.
     * @return Implementación concreta de [RoomUserDao].
     */
    @Provides
    @Singleton
    fun provideUserDao(database: RoomLabDatabase): RoomUserDao {
        return database.userDao()
    }
}

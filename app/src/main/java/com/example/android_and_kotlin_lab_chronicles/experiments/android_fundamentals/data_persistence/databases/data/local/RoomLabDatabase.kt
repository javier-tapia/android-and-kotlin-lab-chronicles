package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.converter.RoomConverters
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.dao.RoomUserDao
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomTagEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.UserTagCrossRefEntity

/**
 * ### Room Database & Estrategia de Migración de Esquemas
 *
 * Contenedor principal de acceso a datos que extiende de [androidx.room.RoomDatabase]. Funciona como la capa
 * de abstracción de nivel superior sobre la base de datos física SQLite subyacente.
 *
 * #### Requisitos de Declaración
 * - **Clase Abstracta (`abstract class`)**: Se declara abstracta porque Room genera dinámicamente
 *   la implementación concreta de esta clase (`RoomLabDatabase_Impl`) durante la fase de compilación
 *   mediante **KSP** (*Kotlin Symbol Processing*).
 * - **Métodos DAO Abstractos (`abstract fun`)**: Cada método encargado de exponer un *Data Access Object*
 *   debe ser abstracto y sin argumentos. Room sobrescribe estas funciones en la clase generada para
 *   retornar las instancias correspondientes de los DAOs.
 *
 * #### Detalle de Anotaciones y Parámetros
 *
 * - **`@Database`**: Identifica la clase como una base de datos de Room.
 *     - `entities`: Array de clases `KClass` anotadas con `@Entity` que conforman el modelo relacional
 *     y **se traducen directamente a tablas físicas dentro de SQLite**.
 *     - `version`: Número entero (`Int`) que define la versión actual del esquema de la base de datos.
 *     Cualquier cambio estructural en las tablas o entidades exige incrementar este valor para activar
 *     el pipeline de migración.
 *     - `exportSchema`: Controla si Room debe exportar la definición del esquema en formato JSON a una
 *     carpeta del proyecto durante la compilación. Útil para verificar cambios de esquema en el control
 *     de versiones (Git). Se establece en `false` para este laboratorio con el fin de simplificar el build.
 *
 * - **`@TypeConverters`**: Registra clases convertidoras globales que transforman tipos de datos no
 *   soportados por SQLite (como `Date`, `UUID` o colecciones) a tipos primitivos persisitibles (`INTEGER`,
 *   `TEXT`, `REAL`, `BLOB`).
 *
 * #### Estrategia de Migración (`Migration`)
 *
 * La clase abstracta [androidx.room.migration.Migration] permite definir la evolución destructiva o no destructiva del
 * esquema SQLite al incrementar la propiedad `version`:
 * - Recibe como parámetros `startVersion` (versión de origen) y `endVersion` (versión de destino).
 * - Ejecuta sentencias DDL (*Data Definition Language*) puras a través de [androidx.sqlite.db.SupportSQLiteDatabase.execSQL]
 *   para alterar tablas (`ALTER TABLE`), crear nuevas estructuras (`CREATE TABLE`) o reconstruir índices
 *   preservando la integridad de los datos existentes.
 *
 * @see RoomUserDao
 * @see RoomUserEntity
 * @see RoomOrderEntity
 * @see RoomConverters
 */
@Database(
    entities = [
        RoomUserEntity::class,
        RoomOrderEntity::class,
        RoomTagEntity::class,
        UserTagCrossRefEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class RoomLabDatabase : RoomDatabase() {

    /**
     * Retorna la implementación del DAO de usuarios generada en tiempo de compilación.
     */
    abstract fun userDao(): RoomUserDao

    companion object {
        /**
         * Migración DDL de la versión 1 a la versión 2.
         *
         * Incorpora la columna `is_active` a la tabla de usuarios existente (`room_users`)
         * asignando un valor por defecto de `1` (`true` en representación booleana de SQLite).
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE room_users ADD COLUMN is_active INTEGER NOT NULL DEFAULT 1")
            }
        }
    }
}

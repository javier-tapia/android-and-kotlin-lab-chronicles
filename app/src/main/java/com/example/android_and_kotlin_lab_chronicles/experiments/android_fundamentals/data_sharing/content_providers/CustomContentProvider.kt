package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.content_providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * Experimento: Creación de un ContentProvider personalizado.
 * 
 * Temas: 
 * - Definición de AUTHORITY y BASE_CONTENT_URI.
 * - Implementación de query, insert, update, delete y getType.
 * - Registro en el AndroidManifest.xml.
 * - Uso de UriMatcher para filtrar peticiones.
 */
class CustomContentProvider : ContentProvider() {
    
    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}
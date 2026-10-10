package com.example.android_and_kotlin_lab_chronicles.core.utils

import android.util.Log
import com.example.android_and_kotlin_lab_chronicles.BuildConfig

object CustomLogger {
    private const val GLOBAL_TAG = "LAB"

    fun log(tag: String, message: String, error: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            if (error == null) {
                Log.d(GLOBAL_TAG, "[$tag] $message")
            } else {
                Log.e(GLOBAL_TAG, "[$tag] $message", error)
            }
        }
    }
}

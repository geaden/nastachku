package com.example.logging

fun loggingLoadLibrary(tag: String, libName: String) {
    simpleLogging(tag) { "Loading library: lib$libName.so" }
}

fun simpleLogging(tag: String, message: () -> String) {
    if (BuildConfig.DEBUG) {
        android.util.Log.d(tag, ">>> ${message()}")
    }
}

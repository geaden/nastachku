package com.example.libloader

import com.example.logging.loggingLoadLibrary
import kotlin.time.Duration
import kotlin.time.measureTime

private const val TAG = "LibLoader"

data class LibraryRecord(
    val name: String,
    val time: Duration,
)

typealias LibraryObserver = () -> Unit

/**
 * Helper object to load library and record it.
 */
object LibLoader {

    private val _loadedLibraries = mutableListOf<LibraryRecord>()

    val loadedLibraries: List<LibraryRecord>
        get() = _loadedLibraries.toList()

    private val observers = mutableListOf<LibraryObserver>()

    @JvmStatic
    fun loadLibrary(name: String) {
        loggingLoadLibrary(TAG, name)
        val time = measureTime {
            System.loadLibrary(name)
        }
        _loadedLibraries += LibraryRecord(name = name, time = time)
        notifyLibraryLoaded()
    }

    fun addObserver(observer: LibraryObserver) {
        observers += observer
    }

    fun removeObserver(observer: LibraryObserver) {
        observers -= observer
    }

    private fun notifyLibraryLoaded() {
        observers.forEach { it() }
    }
}

package com.example.simplejni

import com.example.libloader.LibLoader
import com.example.logging.simpleLogging

private const val TAG = "HelloStachka"

/**
 * Entry point to a simple JNI call.
 */
object HelloStachka {

    init {
        LibLoader.loadLibrary(BuildConfig.LIB_NAME)
    }

    external fun sayHello(name: String): String

    @JvmStatic
    private fun logCall() {
        simpleLogging(TAG) { "Called from native" }
    }
}

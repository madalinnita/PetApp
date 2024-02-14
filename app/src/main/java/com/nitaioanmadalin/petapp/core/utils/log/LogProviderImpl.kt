package com.nitaioanmadalin.petapp.core.utils.log

import android.util.Log
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider

class LogProviderImpl: LogProvider {
    override fun logDebug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }
}
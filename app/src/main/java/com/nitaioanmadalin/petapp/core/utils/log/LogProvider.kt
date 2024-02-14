package com.nitaioanmadalin.petapp.core.utils.log

interface LogProvider {
    fun logDebug(tag: String, message: String)
    fun logError(tag: String, message: String)
    fun logInfo(tag:String, message: String)
}
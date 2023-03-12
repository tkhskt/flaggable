package com.tkhskt.flaggable.internal.logger

interface Logger {

    fun logging(message: String)

    fun info(message: String)

    fun warn(message: String)

    fun error(message: String)

    fun exception(e: Throwable)
}

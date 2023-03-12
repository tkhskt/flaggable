package com.tkhskt.flaggable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Flaggable(val key: String, val name: String)

package com.tkhskt.flaggable.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Flaggable(val key: String, val name: String)

package com.tkhskt.flaggable

import com.squareup.kotlinpoet.ClassName


internal object ClassNames {
    private const val PKG_ANNOTATION = "com.tkhskt.flaggable.annotation"

    val FLAGGABLE = ClassName(PKG_ANNOTATION, "Flaggable")
}

package com.tkhskt.flaggable.internal

import com.squareup.kotlinpoet.ClassName

internal object ClassNames {
    private const val PKG_ANNOTATION = "com.tkhskt.flaggable"
    private const val PKG_COMPOSE_RUNTIME = "androidx.compose.runtime"

    val FLAGGABLE = ClassName(PKG_ANNOTATION, "Flaggable")
    val COMPOSABLE = ClassName(PKG_COMPOSE_RUNTIME, "Composable")
}

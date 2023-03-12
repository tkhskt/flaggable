package com.tkhskt.flaggable.internal.function

import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType

internal class FlaggableComposable(
    val name: String,
    val key: String,
    val annotatedFunctionName: String,
    val packageName: String,
    val parameters: List<Parameter>,
    val containingFile: KSFile?,
) {
    data class Parameter(
        val name: String,
        val type: KSType,
    )

    fun getGroup(): String {
        return packageName + name
    }
}

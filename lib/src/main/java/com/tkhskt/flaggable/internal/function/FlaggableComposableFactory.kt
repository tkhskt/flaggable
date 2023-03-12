package com.tkhskt.flaggable.internal.function

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.tkhskt.flaggable.Flaggable
import com.tkhskt.flaggable.internal.annotation.property.Key
import com.tkhskt.flaggable.internal.annotation.property.Name

internal class FlaggableComposableFactory {

    companion object {
        fun create(functionDeclaration: KSFunctionDeclaration): FlaggableComposable? {
            val name = functionDeclaration.findPropertyValue<String>(Name.NAME) ?: return null
            val key = functionDeclaration.findPropertyValue<String>(Key.NAME) ?: return null
            val parameters = functionDeclaration.parameters.mapNotNull {
                val parameterName = it.name ?: return@mapNotNull null
                FlaggableComposable.Parameter(
                    name = parameterName.asString(),
                    type = it.type.resolve()
                )
            }
            return FlaggableComposable(
                name = name,
                key = key,
                annotatedFunctionName = functionDeclaration.simpleName.asString(),
                packageName = functionDeclaration.packageName.asString(),
                parameters = parameters,
                containingFile = functionDeclaration.containingFile
            )
        }
    }
}

private inline fun <reified T> KSFunctionDeclaration.findPropertyValue(name: String): T? {
    val value = annotations.filter {
        it.shortName.asString() == Flaggable::class.simpleName
    }.flatMap {
        it.arguments
    }.firstOrNull {
        it.name?.asString() == name
    }?.value
    return if (value != null && value is T) {
        value
    } else {
        null
    }
}

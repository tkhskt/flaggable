package com.tkhskt.flaggable.internal.generator

internal object Name {

    fun fileName(name: String): String {
        return "Flaggable$name"
    }

    fun interfaceName(name: String): String {
        return fileName(name)
    }

    fun objectName(annotatedFunctionName: String): String {
        return annotatedFunctionName
    }

    fun containerName(name: String): String {
        return "Flaggable${name}Container"
    }
}

package com.tkhskt.flaggable.internal.annotation.property

internal class Name(
    override val value: String,
) : AnnotationProperty<String> {
    companion object {
        const val NAME = "name"
    }
}

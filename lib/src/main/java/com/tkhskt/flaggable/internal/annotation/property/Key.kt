package com.tkhskt.flaggable.internal.annotation.property

internal class Key(
    override val value: String,
) : AnnotationProperty<String> {
    companion object {
        const val NAME = "key"
    }
}

package com.tkhskt.flaggable

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class FlaggableProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FlaggableProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

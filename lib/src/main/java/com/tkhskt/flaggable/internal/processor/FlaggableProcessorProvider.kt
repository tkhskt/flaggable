package com.tkhskt.flaggable.internal.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tkhskt.flaggable.internal.di.Module
import com.tkhskt.flaggable.internal.logger.KspLogger

internal class FlaggableProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        Module.run {
            logger = KspLogger(environment.logger)
            codeGenerator = environment.codeGenerator
        }
        return FlaggableProcessor()
    }
}

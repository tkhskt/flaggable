package com.tkhskt.flaggable.internal.generator.service

import com.google.devtools.ksp.symbol.KSFile
import com.tkhskt.flaggable.internal.function.FunctionStore
import com.tkhskt.flaggable.internal.generator.ComposableGenerator
import com.tkhskt.flaggable.internal.logger.Logger

internal interface ContainerGenerationService {
    fun generate(containingFiles: List<KSFile>)
}

internal class ContainerGenerationServiceImpl(
    private val functionStore: FunctionStore,
    private val composableGenerator: ComposableGenerator,
    private val logger: Logger,
) : ContainerGenerationService {

    override fun generate(containingFiles: List<KSFile>) {
        try {
            val groupedComposableFunctions = functionStore.getGroupedComposableFunctions()
            groupedComposableFunctions.forEach { (_, functions) ->
                composableGenerator.generate(functions, containingFiles)
            }
        } catch (e: Exception) {
            logger.error(e.message ?: "An unexpected error has occurred.")
        }
    }
}

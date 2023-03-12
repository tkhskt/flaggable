package com.tkhskt.flaggable.internal.generator.service

import com.tkhskt.flaggable.internal.function.FlaggableComposable
import com.tkhskt.flaggable.internal.function.FunctionStore
import com.tkhskt.flaggable.internal.generator.ComposableInterfaceGenerator
import com.tkhskt.flaggable.internal.generator.ComposableObjectGenerator
import com.tkhskt.flaggable.internal.logger.Logger

internal interface ComposableObjectGenerationService {
    fun generate(flaggableComposable: FlaggableComposable)
}

internal class ComposableObjectGenerationServiceImpl(
    private val functionStore: FunctionStore,
    private val interfaceGenerator: ComposableInterfaceGenerator,
    private val objectGenerator: ComposableObjectGenerator,
    private val logger: Logger,
) : ComposableObjectGenerationService {

    override fun generate(flaggableComposable: FlaggableComposable) {
        try {
            val containingFile = flaggableComposable.containingFile ?: return
            val existentComposable = functionStore.findByName(
                flaggableComposable.name,
                flaggableComposable.packageName
            )
            functionStore.addComposableFunction(flaggableComposable)
            if (existentComposable.isEmpty()) {
                interfaceGenerator.generate(flaggableComposable, containingFile)
            }
            objectGenerator.generate(flaggableComposable, containingFile)
        } catch (e: Exception) {
            logger.error(e.message ?: "An unexpected error has occurred.")
        }
    }
}

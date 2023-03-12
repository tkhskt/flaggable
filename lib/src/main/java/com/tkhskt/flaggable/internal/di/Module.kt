package com.tkhskt.flaggable.internal.di

import com.google.devtools.ksp.processing.CodeGenerator
import com.tkhskt.flaggable.internal.function.FunctionStore
import com.tkhskt.flaggable.internal.generator.ComposableGeneratorImpl
import com.tkhskt.flaggable.internal.generator.ComposableInterfaceGeneratorImpl
import com.tkhskt.flaggable.internal.generator.ComposableObjectGeneratorImpl
import com.tkhskt.flaggable.internal.generator.service.ComposableObjectGenerationService
import com.tkhskt.flaggable.internal.generator.service.ComposableObjectGenerationServiceImpl
import com.tkhskt.flaggable.internal.generator.service.ContainerGenerationService
import com.tkhskt.flaggable.internal.generator.service.ContainerGenerationServiceImpl
import com.tkhskt.flaggable.internal.logger.Logger

internal class Module {

    companion object {
        lateinit var logger: Logger

        lateinit var codeGenerator: CodeGenerator

        private val functionStore = FunctionStore()

        private val interfaceGenerator by lazy {
            ComposableInterfaceGeneratorImpl(codeGenerator)
        }

        private val objectGenerator by lazy {
            ComposableObjectGeneratorImpl(codeGenerator)
        }

        private val containerGenerator by lazy {
            ComposableGeneratorImpl(codeGenerator)
        }

        val composableObjectGenerationService: ComposableObjectGenerationService by lazy {
            ComposableObjectGenerationServiceImpl(
                functionStore,
                interfaceGenerator,
                objectGenerator,
                logger
            )
        }

        val containerGenerationService: ContainerGenerationService by lazy {
            ContainerGenerationServiceImpl(functionStore, containerGenerator, logger)
        }
    }
}

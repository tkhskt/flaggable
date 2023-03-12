package com.tkhskt.flaggable.internal.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.tkhskt.flaggable.internal.ClassNames
import com.tkhskt.flaggable.internal.di.Module
import com.tkhskt.flaggable.internal.function.FlaggableComposableFactory
import com.tkhskt.flaggable.internal.generator.service.ComposableObjectGenerationService

internal class FlaggableProcessor : SymbolProcessor {

    private val visitor = FlaggableVisitor(
        composableObjectGenerationService = Module.composableObjectGenerationService
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver.getSymbolsWithAnnotation(ClassNames.FLAGGABLE.canonicalName)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(visitor, Unit) }

        return ret
    }

    private inner class FlaggableVisitor(
        private val composableObjectGenerationService: ComposableObjectGenerationService,
    ) : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val flaggableComposable = FlaggableComposableFactory.create(function) ?: return
            composableObjectGenerationService.generate(flaggableComposable)
        }
    }

    override fun finish() {
        Module.containerGenerationService.generate(emptyList())
    }
}

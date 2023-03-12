package com.tkhskt.flaggable.internal.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.tkhskt.flaggable.internal.ClassNames.COMPOSABLE
import com.tkhskt.flaggable.internal.function.FlaggableComposable

internal interface ComposableInterfaceGenerator {
    fun generate(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    )
}

internal class ComposableInterfaceGeneratorImpl(
    private val codeGenerator: CodeGenerator,
) : ComposableInterfaceGenerator {

    override fun generate(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    ) {
        generateComposableInterface(
            flaggableComposable,
            containingFile
        )
    }

    private fun generateComposableInterface(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    ) {
        val interfaceSpec =
            TypeSpec.interfaceBuilder(Name.interfaceName(flaggableComposable.name))
                .addRenderFunction(flaggableComposable.parameters)
                .build()
        val file = FileSpec
            .builder(
                flaggableComposable.packageName,
                Name.fileName(flaggableComposable.name)
            )
            .addType(interfaceSpec)
            .build()
        file.writeTo(codeGenerator, Dependencies(false, containingFile))
    }

    private fun TypeSpec.Builder.addRenderFunction(parameters: List<FlaggableComposable.Parameter>): TypeSpec.Builder {
        val function = FunSpec.builder(FUNCTION_NAME_COMPOSE)
            .addRenderFunctionParameter(parameters)
            .addComposableAnnotation()
            .addModifiers(KModifier.ABSTRACT)
            .build()
        return addFunction(function)
    }

    private fun FunSpec.Builder.addRenderFunctionParameter(
        parameters: List<FlaggableComposable.Parameter>,
    ): FunSpec.Builder {
        val parameterSpecs = parameters.map { parameter ->
            ParameterSpec
                .builder(parameter.name, parameter.type.toTypeName())
                .build()
        }
        return addParameters(parameterSpecs)
    }

    private fun FunSpec.Builder.addComposableAnnotation(): FunSpec.Builder {
        val annotationSpec = AnnotationSpec
            .builder(COMPOSABLE)
            .build()
        return addAnnotation(annotationSpec)
    }

    companion object {
        private const val FUNCTION_NAME_COMPOSE = "compose"
    }
}

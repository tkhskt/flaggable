package com.tkhskt.flaggable.internal.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.tkhskt.flaggable.internal.ClassNames
import com.tkhskt.flaggable.internal.function.FlaggableComposable

internal interface ComposableObjectGenerator {
    fun generate(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    )
}

internal class ComposableObjectGeneratorImpl(
    private val codeGenerator: CodeGenerator,
) : ComposableObjectGenerator {

    override fun generate(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    ) {
        generateComposableObject(
            flaggableComposable,
            containingFile
        )
    }

    private fun generateComposableObject(
        flaggableComposable: FlaggableComposable,
        containingFile: KSFile,
    ) {
        val objectSpec =
            TypeSpec.objectBuilder(flaggableComposable.annotatedFunctionName)
                .addSuperinterface(
                    ClassName(
                        flaggableComposable.packageName,
                        Name.interfaceName(flaggableComposable.name)
                    )
                )
                .addRenderFunction(flaggableComposable)
                .build()
        val file = FileSpec
            .builder(
                flaggableComposable.packageName,
                flaggableComposable.annotatedFunctionName
            )
            .addType(objectSpec)
            .build()
        file.writeTo(codeGenerator, Dependencies(false, containingFile))
    }

    private fun TypeSpec.Builder.addRenderFunction(flaggableComposable: FlaggableComposable): TypeSpec.Builder {
        val function = FunSpec.builder(FUNCTION_NAME_COMPOSE)
            .addModifiers(KModifier.OVERRIDE)
            .addRenderFunctionParameter(flaggableComposable.parameters)
            .addComposableAnnotation()
            .addCallComposableStatement(flaggableComposable)
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
            .builder(ClassNames.COMPOSABLE)
            .build()
        return addAnnotation(annotationSpec)
    }

    private fun FunSpec.Builder.addCallComposableStatement(flaggableComposable: FlaggableComposable): FunSpec.Builder {
        val statement = buildString {
            append(flaggableComposable.annotatedFunctionName)
            append("(")
            flaggableComposable.parameters.forEachIndexed { index, parameter ->
                if (index > 0) {
                    append(", ")
                }
                append(parameter.name)
            }
            append(")")
        }
        return addStatement(statement)
    }

    companion object {
        private const val FUNCTION_NAME_COMPOSE = "compose"
    }
}

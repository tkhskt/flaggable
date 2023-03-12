package com.tkhskt.flaggable.internal.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.tkhskt.flaggable.internal.ClassNames
import com.tkhskt.flaggable.internal.function.FlaggableComposable

internal interface ComposableGenerator {
    fun generate(
        flaggableComposableFunctions: List<FlaggableComposable>,
        containingFiles: List<KSFile>,
    )
}

internal class ComposableGeneratorImpl(
    private val codeGenerator: CodeGenerator,
) : ComposableGenerator {

    override fun generate(
        flaggableComposableFunctions: List<FlaggableComposable>,
        containingFiles: List<KSFile>,
    ) {
        generateContainer(
            flaggableComposableFunctions,
            containingFiles
        )
    }

    private fun generateContainer(
        flaggableComposableFunctions: List<FlaggableComposable>,
        containingFiles: List<KSFile>,
    ) {
        val firstComposable = flaggableComposableFunctions.first()
        val objectSpec =
            TypeSpec.objectBuilder(Name.containerName(firstComposable.name))
                .addComposableMap(
                    firstComposable.name,
                    firstComposable.packageName,
                    flaggableComposableFunctions
                )
                .build()
        val composableSpec = FunSpec.builder(firstComposable.name)
            .addComposableParameters(firstComposable)
            .addCallComposableStatement(firstComposable)
            .addComposableAnnotation()
            .build()
        val file = FileSpec
            .builder(
                firstComposable.packageName,
                Name.containerName(firstComposable.name)
            )
            .addFunction(composableSpec)
            .addType(objectSpec)
            .build()

        file.writeTo(codeGenerator, Dependencies(false, *(containingFiles.toTypedArray())))
    }

    private fun TypeSpec.Builder.addComposableMap(
        name: String,
        packageName: String,
        flaggableComposableFunctions: List<FlaggableComposable>,
    ): TypeSpec.Builder {
        val initialStatement = buildString {
            append("mapOf(")
            append("\n")
            flaggableComposableFunctions.forEach {
                append("\"${it.key}\" to ${Name.objectName(it.annotatedFunctionName)},\n")
            }
            append(")")
        }
        val propertySpec = PropertySpec.builder(
            PROPERTY_NAME_COMPOSABLE_FUNCTIONS,
            MAP.parameterizedBy(
                String::class.asClassName(),
                ClassName(packageName, Name.interfaceName(name))
            )
        ).initializer(initialStatement).build()
        return addProperty(propertySpec)
    }

    private fun FunSpec.Builder.addComposableParameters(
        flaggableComposable: FlaggableComposable,
    ): FunSpec.Builder {
        val keyParameterSpec = ParameterSpec.builder(PARAMETER_NAME_KEY, String::class).build()
        val flaggableFunctionParameterSpecs = flaggableComposable.parameters.map {
            ParameterSpec.builder(it.name, it.type.toTypeName()).build()
        }
        return addParameters(listOf(keyParameterSpec) + flaggableFunctionParameterSpecs)
    }

    private fun FunSpec.Builder.addComposableAnnotation(): FunSpec.Builder {
        val annotationSpec = AnnotationSpec
            .builder(ClassNames.COMPOSABLE)
            .build()
        return addAnnotation(annotationSpec)
    }

    private fun FunSpec.Builder.addCallComposableStatement(flaggableComposable: FlaggableComposable): FunSpec.Builder {
        val statement = buildString {
            append("${Name.containerName(flaggableComposable.name)}.$PROPERTY_NAME_COMPOSABLE_FUNCTIONS[$PARAMETER_NAME_KEY]?.compose")
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
        private const val PROPERTY_NAME_COMPOSABLE_FUNCTIONS = "composableFunctions"
        private const val PARAMETER_NAME_KEY = "key"
    }
}

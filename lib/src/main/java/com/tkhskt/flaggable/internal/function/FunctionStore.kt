package com.tkhskt.flaggable.internal.function

internal class FunctionStore {

    class DifferentParameterException(
        message: String = "Flaggable annotations with the same Name cannot be set for Composable Functions with different arguments.",
    ) : Exception(message)

    private val _composableFunctions = mutableListOf<FlaggableComposable>()
    private val composableFunctions
        get() = _composableFunctions.toList()

    @Throws(DifferentParameterException::class)
    fun addComposableFunction(flaggableComposable: FlaggableComposable) {
        val sameFlagComposableList = composableFunctions
            .filterByName(flaggableComposable.name)
            .filterByPackageName(flaggableComposable.packageName)
        if (sameFlagComposableList.any { it.parameters != flaggableComposable.parameters }) {
            throw DifferentParameterException()
        }
        _composableFunctions.add(flaggableComposable)
    }

    fun findByKey(key: String): List<FlaggableComposable> {
        return this.composableFunctions.filterByKey(key)
    }

    fun getGroupedComposableFunctions(): Map<String, List<FlaggableComposable>> {
        return composableFunctions.groupBy {
            it.getGroup()
        }
    }

    fun findByName(name: String, packageName: String? = null): List<FlaggableComposable> {
        return this.composableFunctions
            .filterByName(name)
            .run {
                if (packageName != null) filterByPackageName(packageName) else this
            }
    }

    private fun List<FlaggableComposable>.filterByName(name: String): List<FlaggableComposable> {
        return filter { it.name == name }
    }

    private fun List<FlaggableComposable>.filterByKey(key: String): List<FlaggableComposable> {
        return filter { it.key == key }
    }

    private fun List<FlaggableComposable>.filterByPackageName(packageName: String): List<FlaggableComposable> {
        return filter { it.packageName == packageName }
    }
}

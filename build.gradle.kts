plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    @Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")
    kotlin("jvm") version "1.8.10" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.8.10"))
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }
}

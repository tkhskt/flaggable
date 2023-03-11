plugins {
    `maven-publish`
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.stdlib)
    implementation(libs.bundles.kotlin.poet)
    implementation(libs.ksp.processing.api)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("library") {
                from(components["java"])
                group = LibraryInfo.group
                groupId = LibraryInfo.group
                version = LibraryInfo.version
                artifactId = LibraryInfo.artifactId

                pom {
                    name.set(LibraryInfo.artifactId)
                    description.set(LibraryInfo.description)
                    url.set(LibraryInfo.url)

                    scm {
                        connection.set("scm:git@github.com:tkhskt/flaggable.git")
                        developerConnection.set("scm:git@github.com:tkhskt/flaggable.git")
                        url.set(LibraryInfo.url)
                    }

                    developers {
                        developer {
                            id.set("tkhskt")
                            name.set("Keita Takahashi")
                        }
                    }
                }
            }
        }
    }
}

project(":lib") {
    version = LibraryInfo.version
}

object LibraryInfo {
    const val artifactId = "flaggable"
    const val description =
        "Generates DSL to simplify processing branching by when expressions in sealed class/interface."
    const val displayName = "flaggable"
    const val group = "com.tkhskt"
    const val url = "https://github.com/tkhskt/flaggable"
    const val version = "0.1.0"
}

@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.support.listFilesOrdered

rootProject.name = "BiometricWithCrypto"

include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Include core modules
includeProjectsInDir("core")

fun includeProjectsInDir(dirName: String) {
    file(dirName)
        .listFilesOrdered { it.isDirectory }
        .forEach { includeProject(it) }
}

fun includeProject(dir: File) {
    include(dir.name)

    val project = project(":${dir.name}").apply {
        projectDir = dir
        buildFileName = "build.gradle.kts"
    }

    require(project.projectDir.isDirectory) {
        "Project '${project.path} must have a ${project.projectDir} directory"
    }

    require(project.buildFile.isFile) {
        "Project '${project.path} must have a ${project.buildFile} build script"
    }
}
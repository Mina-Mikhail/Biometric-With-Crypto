plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.sonarQube) apply true
}

buildscript {
    apply("config.gradle.kts")
}

sonar {
    properties {
        property(
            "sonar.java.source",
            rootProject.extra.get("javaSource").toString()
        )
        property(
            "sonar.sourceEncoding",
            rootProject.extra.get("sourceEncoding").toString()
        )
        property(
            "sonar.exclusions",
            rootProject.extra.get("sonarCodeExclusion").toString()
        )
        property(
            "sonar.projectKey",
            rootProject.extra.get("sonarProjectKey").toString()
        )
        property(
            "sonar.host.url",
            rootProject.extra.get("sonarHostURL").toString()
        )
        property(
            "sonar.token",
            rootProject.extra.get("sonarLoginToken").toString()
        )
    }
}

subprojects {
    sonar {
        properties {
            property(
                "sonar.sources",
                rootProject.extra.get("srcDirs").toString()
            )
            property(
                "sonar.exclusions",
                rootProject.extra.get("sonarCodeExclusion").toString()
            )

            // In case you use have unit testing
//            property(
//                "sonar.tests",
//                rootProject.extra.get("testDirs").toString()
//            )

            // In case you are using Jacoco
//            property(
//                "sonar.coverage.jacoco.xmlReportPaths",
//                rootProject.extra.get("jacocoReportFilePath").toString()
//            )
        }
    }
}

tasks.register("clean", Delete::class) {
    description = "Remove build directory during clean task"
    group = JavaBasePlugin.DOCUMENTATION_GROUP

    delete(rootProject.layout.buildDirectory)
}
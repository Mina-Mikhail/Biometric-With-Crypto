plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.minaMikhail.biometricWithCrypto"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = rootProject.extra.get("applicationId").toString()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = rootProject.extra.get("testRunner").toString()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                rootProject.extra.get("proguardFileName").toString()
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = libs.versions.jvmVersion.get()

    viewBinding.isEnabled = true

    lint {
        quiet = true
        abortOnError = false
        warningsAsErrors = true
        disable += "Instantiatable"
    }
}

dependencies {

    // Support
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)

    // UI
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Utils
    implementation(libs.gson)

    // Core Modules
    implementation(projects.biometricAuthentication)
    implementation(projects.crypto)
    implementation(projects.prefs)
}
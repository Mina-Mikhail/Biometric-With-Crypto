extra.apply {
    // Config
    set("applicationId", "com.minaMikhail.biometricWithCrypto")
    set("testRunner", "androidx.test.runner.AndroidJUnitRunner")
    set("proguardFileName", "proguard-rules.pro")

    // Build Types
    set("debugBuildType", "debug")

    // Sonarqube
    set("sonarProjectKey", "Crypto-App")
    set("sonarHostURL", "http://localhost:9000")
    set("sonarLoginToken", "sqp_421db01057565424845bf7cefc624c9dc1cfb564")
    set("srcDirs", "src")
    set("testDirs", "src/test")
    set("javaSource", "1.17")
    set("sourceEncoding", "UTF-8")
    set("lintReportFilePath", "build/reports/lint-results-debug.xml")
    set("jacocoReportFilePath", "build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml")
    set("sonarCodeExclusion", getAndroidCodeExclusion())
}

fun getAndroidCodeExclusion(): String {
    val androidCodeExclusion = arrayListOf(
        "**/databinding/**/*.*",
        "**/android/databinding/*Binding.*",
        "**/BR.*",
        "**/R.*",
        "**/R$*.*",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/mockserver/**",
        "*.json"
    )

    return androidCodeExclusion.joinToString(separator = ",")
}
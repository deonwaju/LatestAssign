import java.util.Properties

plugins {
    `core-module-config`
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlinSerialization)
}

val localProperties = Properties()

val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
    localPropertiesFile.inputStream().use { input ->
        localProperties.load(input)
    }
} else {
    logger.warn("local.properties file not found. API keys will not be available.")
}

val keystoreFile: File = project.rootProject.file("gradle.properties")
val properties = Properties()
properties.load(keystoreFile.inputStream())

android {
    namespace = "com.deontch.core.network"

    defaultConfig {        buildConfigField(
        type = "String",
        name = "CHARACTERS_AUTH_TOKEN",
        // This line is correct
        value = "\"${localProperties.getProperty("CHARACTERS_AUTH_TOKEN") ?: ""}\""
    )

        buildConfigField(
            type = "String",
            name = "CHARACTERS_API_BASE_URL",
            // This is the corrected line: removed the extra quote at the start
            value = "\"${localProperties.getProperty("CHARACTERS_API_BASE_URL") ?: ""}\""
        )
    }
}

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:testing"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    api(libs.retrofit)
    api(libs.retrofit.gson)
    api(libs.logging.interceptor)
}

import java.util.Properties

plugins {
    `core-module-config`
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlinSerialization)
}

val keystoreFile: File = project.rootProject.file("gradle.properties")
val properties = Properties()
properties.load(keystoreFile.inputStream())

val charactersAuthToken = properties.getProperty("CHARACTERS_AUTH_TOKEN") ?: ""
val baseUrl = properties.getProperty("CHARACTERS_API_BASE_URL") ?: ""

android {
    namespace = "com.deontch.core.network"

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "CHARACTERS_AUTH_TOKEN",
            value = charactersAuthToken
        )

        buildConfigField(
            type = "String",
            name = "CHARACTERS_API_BASE_URL",
            value = baseUrl
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

plugins {
    `core-module-config`
}

dependencies {
    implementation(project(":core:base"))
    implementation(libs.coroutines.test)

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
// Coroutines testing
    testImplementation(libs.coroutines.test)
// Mocking library (MockK is very Kotlin-friendly)
    testImplementation("io.mockk:mockk:1.13.10")
}

android {
    namespace = "com.deontch.core.testing"
}

plugins {
    `core-module-config`
    id("kotlin-parcelize")
}

android {
    namespace = "com.deontch.models"
}

dependencies {

    annotationProcessor(libs.room.compiler)
    api(libs.room.runtime)
    kapt(libs.kapt.room.compiler)
    api(libs.room.ktx)

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
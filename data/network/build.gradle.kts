

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.network"
}

dependencies {
    implementation(projects.data.common)
    implementation(projects.domain.model)

    // todo
    implementation(platform(libs.okhttpBom))
    implementation(libs.okhttpLoggingInterceptor)
    implementation(libs.okhttp)
    implementation(libs.annotationJvm)

    implementation(libs.bundles.ktor)
    implementation(libs.ktorOkhttp)
    implementation(libs.hiltCore)
    ksp(libs.hiltCompiler)
}

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.db"
}

dependencies {
    implementation(projects.domain.model)

    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)
    implementation(platform(libs.koinBom))
    implementation(libs.koinAndroid)
    testImplementation(libs.bundles.testCore)
}

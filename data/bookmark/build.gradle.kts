plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rosahosseini.findr.data.repository"
}

dependencies {
    implementation(project(":data:db"))
    implementation(project(":domain:bookmark"))
    implementation(project(":domain:model"))
    implementation(project(":library:coroutine"))
    implementation(libs.coroutinesAndroid)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    testImplementation(libs.bundles.testCore)
}

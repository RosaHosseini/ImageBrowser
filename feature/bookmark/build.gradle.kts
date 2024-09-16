plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.feature.bookmark"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.library.coroutine)
    implementation(projects.library.arch)
    implementation(projects.library.ui)
    implementation(projects.domain.model)
    implementation(projects.domain.bookmark)

    implementation(platform(libs.composeBom))
    implementation(libs.composeRuntime)
    implementation(libs.immutableCollections)
    implementation(libs.composeMaterial3)
    implementation(libs.composeNavigation)
    implementation(libs.composeTooling)
    implementation(libs.lifecycleViewModel)
    implementation(libs.lifecycleCompose)
    implementation(platform(libs.koinBom))
    implementation(libs.koinComposeViewmodel)

    testImplementation(libs.bundles.testCore)
}

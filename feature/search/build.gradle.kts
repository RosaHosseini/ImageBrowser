plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.feature.search"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.library.startup)
    implementation(projects.library.coroutine)
    implementation(projects.library.ui)
    implementation(projects.library.arch)
    implementation(projects.domain.model)
    implementation(projects.domain.search)
    implementation(projects.feature.bookmark)

    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeNavigation)
    implementation(libs.immutableCollections)
    implementation(libs.composeMaterial3)
    implementation(libs.composeFlowLayout)
    implementation(libs.composeTooling)
    implementation(libs.workManagerRuntime)
    implementation(platform(libs.koinBom))
    implementation(libs.koinComposeViewmodel)
    implementation(libs.koinWorkManager)

    testImplementation(libs.bundles.testCore)
}

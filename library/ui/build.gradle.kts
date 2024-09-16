plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.library.ui"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.domain.model)

    implementation(platform(libs.composeBom))
    implementation(libs.composeMaterial3)
    implementation(libs.composeNavigation)
    implementation(libs.immutableCollections)
    implementation(libs.composeTooling)
    implementation(libs.composeCoil)

    testImplementation(libs.bundles.testCore)
}

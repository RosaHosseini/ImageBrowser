plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.rosahosseini.findr.feature.photodetail"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.library.coroutine)
    implementation(projects.library.ui)
    implementation(projects.domain.model)

    implementation(platform(libs.composeBom))
    implementation(libs.lifecycleCompose)
    implementation(libs.composeTooling)
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
}

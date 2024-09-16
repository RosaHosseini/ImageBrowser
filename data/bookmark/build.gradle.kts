plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.data.repository"
}

dependencies {
    implementation(projects.data.db)
    implementation(projects.domain.bookmark)
    implementation(projects.domain.model)
    implementation(projects.library.coroutine)

    implementation(libs.coroutinesAndroid)
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)
    testImplementation(libs.bundles.testCore)
}

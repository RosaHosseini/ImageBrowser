plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.data.search"
}

dependencies {
    implementation(projects.data.common)
    implementation(projects.data.db)
    implementation(projects.domain.search)
    implementation(projects.domain.model)
    implementation(projects.library.coroutine)

    implementation(libs.coroutinesCore)
    implementation(libs.gson)
    implementation(libs.ktorCore)
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)

    testImplementation(libs.bundles.testCore)
}

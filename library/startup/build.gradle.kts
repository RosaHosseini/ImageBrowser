plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.rosahosseini.findr.library.startup"
}

dependencies {
    implementation(platform(libs.koinBom))
    implementation(libs.coroutinesCore)
    implementation(libs.koinCore)
}

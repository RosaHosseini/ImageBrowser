plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.coroutinesCore)
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)
}

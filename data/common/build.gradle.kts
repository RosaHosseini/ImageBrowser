plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.domain.model)
    implementation(libs.gson)
    implementation(libs.ktorCore)
    implementation(platform(libs.koinBom))
    api(libs.koinCore)
}

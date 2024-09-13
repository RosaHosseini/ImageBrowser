plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.domain.model)
    implementation(libs.gson)
    implementation(libs.hiltCore)
    implementation(libs.ktorCore)
}

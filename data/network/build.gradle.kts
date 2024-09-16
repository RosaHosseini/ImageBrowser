plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.data.common)
    implementation(projects.domain.model)

    implementation(libs.bundles.ktor)
    implementation(libs.ktorOkhttp)
    implementation(platform(libs.koinBom))
    implementation(libs.koinCore)
}

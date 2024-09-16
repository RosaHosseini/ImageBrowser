plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.domain.model)
    implementation(libs.coroutinesCore)
}

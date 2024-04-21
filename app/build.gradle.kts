import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val apiKeyProperties = loadIntoProjectProperties(filePath = "apikey.properties")
val versionProperties = loadIntoProjectProperties(filePath = "version.properties")

android {
    namespace = "com.rosahosseini.findr.app"

    defaultConfig {
        applicationId = "com.rosahosseini.findr"
        versionCode = (versionProperties["appVersionCode"] as String?)?.toInt() ?: 1
        versionName = versionProperties["appVersionName"] as String? ?: "0.0.1"
        buildConfigField(
            "String",
            "FLICKR_API_KEY",
            apiKeyProperties["FLICKR_KEY"] as String? ?: ""
        )
    }

    defaultConfig {
        applicationId = "nl.emesa.actievandedag"

        // uncomment if need to use hilt runner instead for hilt support for instrumentation tests
        // testInstrumentationRunner = "com.emesa.avdd.library.testing.android.AvddHiltTestRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md",
            "META-INF/**previous-compilation-data.bin",
            "DebugProbesKt.bin",
            "META-INF/proguard/*",
            "/*.properties",
            "META-INF/*.properties"
        )
    }
}

dependencies {
    implementation(project(":library:ui"))
    implementation(project(":library:startup"))
    implementation(project(":data:network"))
    implementation(project(":data:search"))
    implementation(project(":data:bookmark"))
    implementation(project(":domain:model"))
    implementation(project(":feature:search"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:photodetail"))
    implementation(libs.appcompat)
    implementation(platform(libs.composeBom))
    implementation(libs.hiltNavigationCompose)
    implementation(libs.composeActivity)
    implementation(libs.composeMaterial3)
    implementation(libs.accompanistInsets)
    implementation(libs.workManagerHilt)
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
}

// Load keys into project properties
fun loadIntoProjectProperties(filePath: String): Properties {
    val propertiesFile: File = rootProject.file(filePath)
    val properties = Properties()
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use(properties::load)
    }
    return properties
}

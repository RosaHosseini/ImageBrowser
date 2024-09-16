import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
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
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(projects.library.ui)
    implementation(projects.library.startup)
    implementation(projects.library.coroutine)
    implementation(projects.data.network)
    implementation(projects.data.db)
    implementation(projects.data.search)
    implementation(projects.data.bookmark)
    implementation(projects.domain.model)
    implementation(projects.feature.search)
    implementation(projects.feature.bookmark)
    implementation(projects.feature.photodetail)
    implementation(libs.appcompat)
    implementation(platform(libs.composeBom))
    implementation(libs.composeActivity)
    implementation(libs.composeNavigation)
    implementation(libs.composeMaterial3)
    implementation(libs.accompanistInsets)
    implementation(platform(libs.koinBom))
    implementation(libs.koinAndroid)
    implementation(libs.koinWorkManager)
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

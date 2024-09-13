import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

val javaVersion = JavaVersion.VERSION_17

plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.composeCompiler).apply(false)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinJvm).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.ktlint).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.room).apply(false)
}

allprojects {

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    configureKtlint()
    configureDetekt()
    configureTestTasks()
    configureJava()
    configureAndroidProjects()
}

fun Project.configureKtlint() {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    configure<KtlintExtension> {
        version.set(rootProject.libs.versions.ktlint.get())
        android.set(true)
        filter {
            exclude { it.file.absolutePath.contains("/test/") }
        }
        outputToConsole.set(true)
        outputColorName.set("RED")
        enableExperimentalRules.set(true)
    }
}

fun Project.configureDetekt() {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    configure<DetektExtension> {
        config.setFrom("$rootDir/detekt/detekt.yml")
        parallel = true
        autoCorrect = false
    }
    tasks.withType<Detekt>().configureEach {
        jvmTarget = javaVersion.majorVersion
        reports {
            html.required.set(false)
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }

    dependencies {
        detektPlugins(rootProject.libs.detektCompose)
    }
}

fun Project.configureAndroidProjects() {
    pluginManager.withPlugin(rootProject.libs.plugins.androidApplication.get().pluginId) {
        configureAndroidVersions()
    }
    pluginManager.withPlugin(rootProject.libs.plugins.androidLibrary.get().pluginId) {
        configureAndroidVersions()
    }
}

fun Project.configureAndroidVersions() {
    configure<BaseExtension> {
        compileSdkVersion(34)
        defaultConfig {
            minSdk = 23
            targetSdk = 34
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }
}

fun Project.configureJava() {
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(javaVersion.majorVersion)
            }
        }
    }
}

fun Project.configureTestTasks() {
    if (hasUnitTests()) {
        tasks.register("unitTests") {
            dependsOn(if (isAndroidModule()) "testDebugUnitTest" else "test")
        }
    }
    tasks.withType<Test>().apply {
        configureEach {
            useJUnitPlatform()
            failFast = true
            maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
        }
    }
}

fun Project.isAndroidModule(): Boolean {
    val isAndroidLibrary = project.plugins.hasPlugin("com.android.library")
    val isAndroidApp = project.plugins.hasPlugin("com.android.application")
    return isAndroidLibrary || isAndroidApp
}

fun Project.hasUnitTests(): Boolean {
    return filesExist(path = "${projectDir.absolutePath}/src/test")
}

fun filesExist(
    path: String,
    supportedExtensions: Set<String> = setOf("kt", "java")
): Boolean {
    val dir = file(path)
    return dir.walkTopDown().any { file -> file.extension in supportedExtensions }
}

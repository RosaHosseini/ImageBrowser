import com.android.build.api.dsl.LibraryExtension
import com.rosahosseini.findr.buildlogic.androidLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension
) = extension.apply {
    
    //get module name from module path
    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace = if(moduleName.isNotEmpty()) "com.rosahosseini.findr.$moduleName" else "com.rosahosseini.findr"

    compileSdk = androidLibs.findVersion("compileSdk").get().requiredVersion.toInt()
    defaultConfig {
        minSdk = androidLibs.findVersion("minSdk").get().requiredVersion.toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal fun Project.configureKotlinCocoapods(
    extension: CocoapodsExtension
) = extension.apply {
    val moduleName = this@configureKotlinCocoapods.path.split(":").drop(1).joinToString("-")
    version = "1.0" //your cocoapods version
    ios.deploymentTarget = "14.1" //your iOS deployment target
    name = moduleName
    framework {
        isStatic = true //static or dynamic according to your project
        baseName = moduleName
    }
}
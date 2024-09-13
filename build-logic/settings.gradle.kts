dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("androidLibs") {
            from(files("../gradle/android-libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
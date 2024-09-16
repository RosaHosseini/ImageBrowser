package com.rosahosseini.findr.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs
    get(): VersionCatalog = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

internal val Project.androidLibs
    get(): VersionCatalog = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("androidLibs")
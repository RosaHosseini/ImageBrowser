package com.rosahosseini.findr.startup.di

import com.rosahosseini.findr.startup.StartupTask
import org.koin.dsl.module

val startUpTasksModule = module {
    factory<Set<StartupTask>> { getAll<StartupTask>().toSet() }
}

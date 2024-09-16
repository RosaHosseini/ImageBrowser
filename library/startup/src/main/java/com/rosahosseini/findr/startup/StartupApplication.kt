package com.rosahosseini.findr.startup

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class StartupApplication : Application(), KoinComponent {

    private val startupTasks by inject<Set<StartupTask>>()
    private var isStartupTasksReleased = false

    override fun onCreate() {
        super.onCreate()
        runStartupTasks()
    }

    private fun runStartupTasks() {
        if (isStartupTasksReleased) return
        startupTasks.forEach { taskEntry ->
            MainScope().launch(Dispatchers.Default) {
                taskEntry.run()
            }
        }
        isStartupTasksReleased = true
    }
}

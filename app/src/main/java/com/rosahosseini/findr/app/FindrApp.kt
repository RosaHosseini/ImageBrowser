package com.rosahosseini.findr.app

import com.rosahosseini.findr.app.di.initKoin
import com.rosahosseini.findr.startup.StartupApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent

class FindrApp : StartupApplication(), KoinComponent {

    override fun onCreate() {
        initKoin {
            androidContext(this@FindrApp)
            workManagerFactory()
        }
        super.onCreate()
    }
}

package com.rosahosseini.findr.app.di

import com.rosahosseini.findr.data.bookmark.di.bookmarkDataModule
import com.rosahosseini.findr.data.network.di.httpEngineModule
import com.rosahosseini.findr.data.network.di.networkModule
import com.rosahosseini.findr.data.search.di.searchDataModule
import com.rosahosseini.findr.db.di.localModule
import com.rosahosseini.findr.feature.bookmark.di.bookmarkFeatureModule
import com.rosahosseini.findr.feature.search.di.searchFeatureModule
import com.rosahosseini.findr.feature.search.startups.startUpModule
import com.rosahosseini.findr.library.coroutines.di.coroutineModule
import com.rosahosseini.findr.startup.di.startUpTasksModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            localModule,
            httpEngineModule,
            networkModule,
            bookmarkDataModule,
            searchDataModule,
            bookmarkFeatureModule,
            searchFeatureModule,
            startUpModule,
            coroutineModule,
            startUpTasksModule
        )
    }
}

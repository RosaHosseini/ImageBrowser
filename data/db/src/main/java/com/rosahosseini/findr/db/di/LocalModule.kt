package com.rosahosseini.findr.db.di

import com.rosahosseini.findr.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single { AppDatabase.buildDatabase(androidContext()) }

    single { get<AppDatabase>().photoDao() }
    single { get<AppDatabase>().searchHistoryDao() }
}

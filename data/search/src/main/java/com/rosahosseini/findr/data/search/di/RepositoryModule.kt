package com.rosahosseini.findr.data.search.di

import com.rosahosseini.findr.data.search.local.SearchHistoryLocalDataSource
import com.rosahosseini.findr.data.search.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.findr.data.search.repository.DefaultSearchRepository
import com.rosahosseini.findr.di.flickerUrlQualifier
import com.rosahosseini.findr.domain.search.SearchRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    factoryOf(::DefaultSearchRepository) bind SearchRepository::class
    singleOf(::SearchHistoryLocalDataSource)
    factory { PhotoRemoteDataSource(get(), get(flickerUrlQualifier), get()) }
}

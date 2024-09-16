package com.rosahosseini.findr.data.bookmark.di

import com.rosahosseini.findr.data.bookmark.local.BookmarkLocalDataSource
import com.rosahosseini.findr.data.bookmark.repository.DefaultBookmarkRepository
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bookmarkDataModule = module {
    factoryOf(::DefaultBookmarkRepository) bind BookmarkRepository::class
    singleOf(::BookmarkLocalDataSource)
}

package com.rosahosseini.findr.feature.bookmark.di

import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val bookmarkFeatureModule = module {
    viewModelOf(::BookmarkViewModel)
}

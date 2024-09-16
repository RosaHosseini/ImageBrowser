package com.rosahosseini.findr.feature.search.di

import com.rosahosseini.findr.feature.search.viewmodel.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchFeatureModule = module {
    viewModel { SearchViewModel(get()) }
}

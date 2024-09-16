package com.rosahosseini.findr.library.coroutines.di

import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coroutineModule = module {
    factory {
        CoroutineDispatchers(
            Dispatchers.Main,
            Dispatchers.Default,
            Dispatchers.IO
        )
    }
}

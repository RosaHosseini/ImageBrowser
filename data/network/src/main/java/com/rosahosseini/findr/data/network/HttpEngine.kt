package com.rosahosseini.findr.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

fun configureHttpEngine(): HttpClientEngine = OkHttp.create()

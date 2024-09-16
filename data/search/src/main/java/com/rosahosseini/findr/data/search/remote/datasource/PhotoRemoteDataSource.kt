package com.rosahosseini.findr.data.search.remote.datasource

import com.rosahosseini.findr.data.search.remote.response.SearchPhotosDto
import com.rosahosseini.findr.data.search.remote.response.SearchResponseDto
import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.remote.extensions.catchResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class PhotoRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    buildConfiguration: BuildConfiguration
) {
    private val apiKey = buildConfiguration.flickerApiKey

    suspend fun search(
        text: String? = null,
        limit: Int = 10,
        page: Int = 0
    ): Result<SearchPhotosDto> {
        return catchResult<SearchResponseDto> {
            httpClient
                .get(baseUrl) {
                    parameter("method", "flickr.photos.search")
                    parameter("nojsoncallback", "1")
                    parameter("format", "json")
                    parameter("api_key", apiKey)
                    text?.let { parameter("text", it) }
                    parameter("page", page + 1)
                    parameter("per_page", limit)
                    parameter("extras", URLS)

                    contentType(ContentType.Application.Json)
                }
        }.map { it.getOrThrow() }
    }

    suspend fun getRecent(
        limit: Int = 10,
        page: Int = 0
    ): Result<SearchPhotosDto> {
        return catchResult<SearchResponseDto> {
            httpClient
                .get(baseUrl) {
                    parameter("method", "flickr.photos.getRecent")
                    parameter("nojsoncallback", "1")
                    parameter("format", "json")
                    parameter("api_key", apiKey)
                    parameter("page", page + 1)
                    parameter("per_page", limit)
                    parameter("extras", URLS)

                    contentType(ContentType.Application.Json)
                }
        }.mapCatching { it.getOrThrow() }
    }

    companion object {
        private const val URLS =
            "url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"
    }
}

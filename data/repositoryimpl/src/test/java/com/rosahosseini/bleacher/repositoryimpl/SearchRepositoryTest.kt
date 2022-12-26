package com.rosahosseini.bleacher.repositoryimpl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.rosahosseini.bleacher.commontest.CoroutineTestRule
import com.rosahosseini.bleacher.commontest.coroutineTestCase
import com.rosahosseini.bleacher.local.datasource.SearchPhotoLocalDataSource
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.bleacher.repository.SearchRepository
import com.rosahosseini.bleacher.repositoryimpl.map.toPagePhotos
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import org.junit.Rule
import org.junit.Test

class SearchRepositoryTest {
    private val photoRemoteDataSource: PhotoRemoteDataSource = mock()
    private val searchLocalDataSource: SearchPhotoLocalDataSource = mock()
    private val repository: SearchRepository by lazy {
        SearchRepositoryImpl(photoRemoteDataSource, searchLocalDataSource)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `on searchPhotos gets data from db if it isn't outdated`() = coroutineTestCase {
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn photoList
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verifyZeroInteractions(photoRemoteDataSource)
            assert(response is Either.Success)
            assertEquals(response?.data, photoList.toPagePhotos(5))
        }
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is outdated`() = coroutineTestCase {
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn photoList
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is empty`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on searchPhotos gets data db again if remote throw exception`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doThrow RuntimeException("")
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource, times(2)).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Error)
        }
    }

    @Test
    fun `on getRecentPhotos gets data from db if it isn't outdated`() = coroutineTestCase {
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn photoList
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verifyZeroInteractions(photoRemoteDataSource)
            assert(response is Either.Success)
            assertEquals(response?.data, photoList.toPagePhotos(5))
        }
    }

    @Test
    fun `on getRecentPhotos gets data from remote if db data is outdated`() = coroutineTestCase {
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn photoList
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            verify(searchLocalDataSource).saveSearch(any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on getRecentPhotos gets data from remote if db data is empty`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            verify(searchLocalDataSource).saveSearch(any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on getRecentPhotos gets data db again if remote throw exception`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doThrow RuntimeException("")
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource, times(2)).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            assert(response is Either.Error)
        }
    }

    @Test
    fun `searchLocalPhotos calls local datasource`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.searchFlow(any())
            ) doReturn flowOf(listOf(searchedPhotoEntity, searchedPhotoEntity))
        }
        whenever {
            repository.searchLocalPhotos("").last()
        }
        then {
            verify(searchLocalDataSource).searchFlow("")
            verifyZeroInteractions(photoRemoteDataSource)
        }
    }
}
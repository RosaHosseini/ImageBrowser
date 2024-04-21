package com.rosahosseini.findr.search.viewmodel

import app.cash.turbine.test
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract
import com.rosahosseini.findr.feature.search.viewmodel.SearchViewModel
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.PagingState
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val searchRepository: SearchRepository = mockk()
    private val viewModel: SearchViewModel by lazy { SearchViewModel(searchRepository) }

    private val photo = Photo("", null, null, "", null)

    private fun pagedPhoto(items: List<Photo> = emptyList(), pageNumber: Int = 0) = Page(
        items, pageNumber, pageSize = 1, hasNext = true
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        coEvery {
            searchRepository.getRecentPhotos(0, any())
        } returns Result.success(pagedPhoto(pageNumber = 0))
        coEvery {
            searchRepository.getSearchSuggestion(any(), any())
        } returns emptyFlow()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryTextChange_Intent if term is not empty, viewModel should call searchPhotos update state_photos to success`() {
        runBlocking {
            // given
            val term = "test"
            coEvery {
                searchRepository.searchPhotos(term, 0, any())
            } coAnswers {
                delay(10)
                Result.success(pagedPhoto(pageNumber = 0, items = listOf(photo)))
            }
            coEvery {
                searchRepository.saveSearchQuery(any())
            } just runs

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

            // then
            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(term=term)
                awaitItem() shouldBeEqualTo SearchContract.State(
                   photos = PagingState(
                       status = PagingState.Status.Loading,
                       pageNumber = 0
                   ),
                    term = term
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        status = PagingState.Status.Success,
                        pageNumber = 0,
                        data = persistentListOf(photo),
                        exhausted = false,
                        throwable = null
                    ),
                    term = term
                )
                expectNoEvents()
            }
            coVerify(exactly = 1) { searchRepository.searchPhotos(term, 0, any()) }
        }
    }

    @Test
    fun `onQueryTextChange_Intent if term is not empty, viewModel should call searchPhotos and update state_photos to failure`() {
        runBlocking {
            // given
            val term = "test"
            val throwable = Throwable("test")
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } coAnswers {
                delay(10)
                Result.failure(throwable)
            }
            coEvery {
                searchRepository.saveSearchQuery(any())
            } just runs

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

            // then
            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(term=term)
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        status = PagingState.Status.Loading,
                        pageNumber = 0
                    ),
                    term = term
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        status = PagingState.Status.Failure,
                        pageNumber = 0,
                        data = persistentListOf(),
                        exhausted = false,
                        throwable = throwable
                    ),
                    term = term
                )
                expectNoEvents()
            }
            coVerify(exactly = 1) { searchRepository.searchPhotos(term, 0, any()) }
        }
    }


    @Test
    fun `onQueryTextChange_Intent if term is empty, viewModel should call getRecentPhotos update state_photos to success`() {
        runBlocking {
            // given
            coEvery {
                searchRepository.getRecentPhotos(0, any())
            } coAnswers {
                delay(10)
                Result.success(pagedPhoto(pageNumber = 0, items = listOf(photo)))
            }

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange(""))

            // then
            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(
                    term = ""
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        status = PagingState.Status.Loading,
                        pageNumber = 0
                    ),
                    term = ""
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        status = PagingState.Status.Success,
                        pageNumber = 0,
                        data = persistentListOf(photo),
                        exhausted = false,
                        throwable = null
                    ),
                    term = ""
                )
                expectNoEvents()
            }
            coVerify { searchRepository.getRecentPhotos(0, any()) }
        }
    }

    @Test
    fun `onQueryTextChange_Intent if term is empty, viewModel should call getRecentPhotos and update state_photos to failure`() {
        runBlocking {
            // given
            val throwable = Throwable("test")
            coEvery {
                searchRepository.getRecentPhotos(any(), any())
            } coAnswers {
                delay(10)
                Result.failure(throwable)
            }

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange(""))

            // then
            viewModel.state.test {
                awaitItem()
                awaitItem().photos shouldBeEqualTo PagingState(
                    status = PagingState.Status.Loading,
                    pageNumber = 0
                )
                awaitItem().photos shouldBeEqualTo PagingState(
                    status = PagingState.Status.Failure,
                    pageNumber = 0,
                    data = persistentListOf(),
                    exhausted = false,
                    throwable = throwable
                )
                expectNoEvents()
            }
            coVerify { searchRepository.getRecentPhotos(0, any()) }
        }
    }

    @Test
    fun `onQueryTextChange_Intent viewModel should update state_term`(): Unit = runBlocking {
        // given
        val term = "test"
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns Result.success(pagedPhoto())
        coEvery {
            searchRepository.saveSearchQuery(any())
        } just runs

        // whenever
        viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

        // then
        viewModel.state.value.term shouldBeEqualTo term
    }

    @Test
    fun `On OnQueryTextChange_Intent viewModel should update suggestions`() {
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } returns Result.success(pagedPhoto())
            coEvery {
                searchRepository.getSearchSuggestion("term", any())
            } returns flowOf(listOf("term", "term1", ""))
            coEvery {
                searchRepository.saveSearchQuery(any())
            } just runs

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term"))

            // then
            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(
                    suggestions = persistentListOf("term1"),
                    term = "term"
                )
            }
            coVerify(exactly = 1) { searchRepository.getSearchSuggestion("term", any()) }
        }
    }


    @Test
    fun `On OnQueryTextChange_Intent save query in searchHistory if its not blank`() {
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } returns Result.success(pagedPhoto())
            coEvery {
                searchRepository.saveSearchQuery(any())
            } just runs


            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term1"))

            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(
                    term = "term1"
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    term = "term1",
                    photos = PagingState(status = PagingState.Status.Success)
                )
                expectNoEvents()
            }

            // then
            coVerify(exactly = 1) { searchRepository.saveSearchQuery("term1") }
        }
    }

    @Test
    fun `On OnQueryTextChange_Intent does not save query in searchHistory if it is blank`() {
        runBlocking {
            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange(""))

            // then
            coVerify(exactly = 0) { searchRepository.saveSearchQuery("") }
        }
    }


    @Test
    fun `On OnLoadMore_Intent if term is not empty viewModel should call searchPhotos with nextPage and update state`() {
        runBlocking {
            // given
            val photo1 = photo.copy(id = "1")
            val photo2 = photo.copy(id = "2")
            coEvery {
                searchRepository.searchPhotos(any(), pageNumber = 0, pageSize = any())
            } returns Result.success(pagedPhoto(pageNumber = 0, items = listOf(photo1)))

            coEvery {
                searchRepository.searchPhotos(any(), pageNumber = 1, pageSize = any())
            } returns Result.success(pagedPhoto(pageNumber = 1, items = listOf(photo2)))

            coEvery {
                searchRepository.saveSearchQuery("term")
            } just runs


            //  whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term"))
            delay(1000)
            viewModel.onIntent(SearchContract.Intent.OnLoadMore)

            // then
            coVerify(ordering = Ordering.ORDERED) {
                searchRepository.searchPhotos(
                    query = eq("term"),
                    pageNumber = eq(0),
                    pageSize = any()
                )
                searchRepository.searchPhotos(
                    query = eq("term"),
                    pageNumber = eq(1),
                    pageSize = any()
                )
            }

            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        pageNumber = 1,
                        data = persistentListOf(photo1, photo2),
                        status = PagingState.Status.Success
                    ),
                    term = "term"
                )
                expectNoEvents()
            }
        }
    }


    @Test
    fun `On OnRetry_Intent with failure viewModel should call searchPhotos with current-page and update state`() {
        runBlocking {
            // given
            val throwable = Throwable()
            coEvery {
                searchRepository.searchPhotos(any(), pageNumber = 0, pageSize = any())
            } returnsMany listOf(
                Result.failure(throwable),
                Result.success(pagedPhoto(pageNumber = 0, items = listOf(photo)))
            )

            coEvery { searchRepository.saveSearchQuery("term") } just runs

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term"))

            // then
            viewModel.state.test {
                awaitItem() shouldBeEqualTo SearchContract.State(
                    term = "term"
                )
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        pageNumber = 0,
                        status = PagingState.Status.Failure,
                        throwable = throwable
                    ),
                    term = "term"
                )
                viewModel.onIntent(SearchContract.Intent.OnRetry)
                awaitItem() shouldBeEqualTo SearchContract.State(
                    photos = PagingState(
                        pageNumber = 0,
                        data = persistentListOf(photo),
                        status = PagingState.Status.Success
                    ),
                    term = "term"
                )
                expectNoEvents()
            }

            coVerify(exactly = 2) {
                searchRepository.searchPhotos(
                    query = eq("term"),
                    pageNumber = eq(0),
                    pageSize = any()
                )
            }
        }
    }


    @Test
    fun `On OnLoadMore_Intent if current page is last page viewModel does not search anything`() {
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos("term", pageNumber = 0, pageSize = any())
            } returns Result.success(
                Page(
                    items = listOf(photo),
                    pageNumber = 0,
                    pageSize = 1,
                    hasNext = false
                )
            )
            coEvery { searchRepository.saveSearchQuery("term") } just runs

            val state = PagingState(
                pageNumber = 0,
                data = persistentListOf(photo),
                status = PagingState.Status.Success,
                exhausted = true
            )

            viewModel.onIntent(SearchContract.Intent.OnTermChange("term"))
            viewModel.state.test {
                awaitItem()
                awaitItem().photos shouldBeEqualTo state
                expectNoEvents()
            }

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnLoadMore)

            // then
            viewModel.state.value.photos shouldBeEqualTo state

            coVerify(exactly = 1) {
                searchRepository.searchPhotos(
                    query = eq("term"),
                    pageNumber = eq(0),
                    pageSize = any()
                )
            }
            viewModel.state.value.term shouldBeEqualTo "term"
        }
    }

    @Test
    fun `on OnRemoveSuggestion_Intent, viewmodel should call removeSearchQuery`() = runBlocking {
        // given
        val term = "term"
        coEvery { searchRepository.removeSearchQuery(term) } just runs

        // whenever
        viewModel.onIntent(SearchContract.Intent.OnRemoveSuggestion(term))

        // then
        coVerify(exactly = 1) { searchRepository.removeSearchQuery(term) }
    }
}
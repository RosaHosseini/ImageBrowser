package com.rosahosseini.findr.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.library.arch.MviViewModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.PagingState
import com.rosahosseini.findr.ui.state.onFailure
import com.rosahosseini.findr.ui.state.onLoading
import com.rosahosseini.findr.ui.state.onRefresh
import com.rosahosseini.findr.ui.state.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
internal class SearchViewModel @Inject internal constructor(
    private val searchRepository: SearchRepository
) : MviViewModel<SearchContract.Intent, SearchContract.State>, ViewModel() {

    private val searchRequestsChannel = Channel<SearchQueryIntent>(capacity = BUFFERED)
    private val suggestionRequestChannel = Channel<String>(capacity = BUFFERED)
    private val _state = MutableStateFlow(SearchContract.State())
    override val state: StateFlow<SearchContract.State> = _state

    override fun onIntent(intent: SearchContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is SearchContract.Intent.OnRemoveSuggestion -> onRemoveSuggestion(intent.term)
                is SearchContract.Intent.OnTermChange -> onTermChange(intent.term)
                is SearchContract.Intent.OnLoadMore -> onLoadMorePhotos()
                is SearchContract.Intent.OnRefresh -> onRefresh()
                is SearchContract.Intent.OnRetry -> onLoadMorePhotos()
            }
        }
    }

    init {
        searchRequestsChannel
            .consumeAsFlow()
            .debounce { if (it.isNewTerm) 500 else 0 }
            .onEach { if (it.isNewTerm) saveTermToHistory(it.term) }
            .flatMapLatest(::loadPhotos)
            .onEach { newPagingState -> _state.update { it.copy(photos = newPagingState) } }
            .launchIn(viewModelScope)

        suggestionRequestChannel
            .consumeAsFlow()
            .flatMapLatest(::loadSuggestions)
            .onEach { suggestions -> _state.update { it.copy(suggestions = suggestions) } }
            .launchIn(viewModelScope)

        onIntent(SearchContract.Intent.OnTermChange(""))
    }

    private suspend fun onLoadMorePhotos() = with(state.value) {
        val nextPage = photos.nextPage ?: return
        searchRequestsChannel.send(SearchQueryIntent(term, nextPage))
    }

    private suspend fun onTermChange(newValue: String) {
        _state.update { it.copy(term = newValue) }
        suggestionRequestChannel.send(newValue)
        searchRequestsChannel.send(SearchQueryIntent(term = newValue))
    }

    private suspend fun onRefresh() {
        searchRequestsChannel.send(
            SearchQueryIntent(term = state.value.term, refreshing = true)
        )
    }

    private suspend fun onRemoveSuggestion(term: String) {
        searchRepository.removeSearchQuery(term)
    }

    private suspend fun saveTermToHistory(term: String) {
        viewModelScope.launch {
            if (term.isNotBlank()) searchRepository.saveSearchQuery(term)
        }
    }

    private fun loadSuggestions(term: String): Flow<ImmutableList<String>> {
        return searchRepository
            .getSearchSuggestion(term, limit = 6)
            .map { list -> list.filterNot { it == term || it.isEmpty() }.toImmutableList() }
    }

    private fun loadPhotos(intent: SearchQueryIntent): Flow<PagingState<Photo>> = flow {
        if (intent.refreshing) {
            emit(state.value.photos.onRefresh())
        } else {
            emit(state.value.photos.onLoading(intent.pageNumber))
        }
        if (intent.term.isBlank()) {
            searchRepository.getRecentPhotos(intent.pageNumber, PAGE_SIZE)
        } else {
            searchRepository.searchPhotos(intent.term, intent.pageNumber, PAGE_SIZE)
        }
            .onSuccess { newPage -> emit(state.value.photos.onSuccess(newPage)) }
            .onFailure { throwable -> emit(state.value.photos.onFailure(throwable)) }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}

private data class SearchQueryIntent(
    val term: String,
    val pageNumber: Int = 0,
    val refreshing: Boolean = false
) {
    val isNewTerm: Boolean = pageNumber == 0 && !refreshing
}

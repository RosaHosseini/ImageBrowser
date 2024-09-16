package com.rosahosseini.findr.feature.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.library.arch.MviViewModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.UiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel(), MviViewModel<BookmarkContract.Intent, BookmarkContract.State> {

    private val _state = MutableStateFlow(BookmarkContract.State())
    override val state: StateFlow<BookmarkContract.State> = _state
    private val bookmarkedPhotos: Flow<List<Photo>> = bookmarkRepository.getAllBookmarkedPhotos()

    override fun onIntent(intent: BookmarkContract.Intent) {
        viewModelScope.launch {
            when (intent) {
                is BookmarkContract.Intent.OnUpdateBookmark -> onUpdateBookmark(
                    intent.photo,
                    intent.enabled
                )
            }
        }
    }

    init {
        loadBookmarks()
        loadPhotos()
    }

    private suspend fun onUpdateBookmark(photo: Photo, enabled: Boolean) {
        bookmarkRepository.changeBookmarkState(photo, enabled)
    }

    private fun loadBookmarks() {
        bookmarkedPhotos
            .mapLatest { list -> list.associate { it.id to true }.toImmutableMap() }
            .onEach { bookmarks -> _state.update { it.copy(bookmarks = bookmarks) } }
            .launchIn(viewModelScope)
    }

    private fun loadPhotos() {
        _state.update { it.copy(photos = UiState.Loading(null)) }
        viewModelScope.launch {
            val photos = bookmarkedPhotos.first()
            _state.update { it.copy(photos = UiState.Success(photos.toImmutableList())) }
        }
    }
}

package com.rosahosseini.findr.bookmark.viewmodel

import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.core.base.BaseViewModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.navigation.Navigator
import com.rosahosseini.findr.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.findr.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    val bookmarkedPhotos: StateFlow<List<Photo>> = bookmarkRepository.getAllBookmarkedPhotos()
        .stateIn(emptyList())

    fun onToggleBookmark(photo: Photo) {
        viewModelScope.launch {
            bookmarkRepository.changeBookmarkState(photo.id, photo.isBookmarked.not())
        }
    }

    fun onPhotoClick(photo: Photo) {
        viewModelScope.launch {
            navigator.navigateTo(
                navTarget = PhotoDetailDestination,
                args = listOf(Pair(PhotoDetailDestination.arg, photo))
            )
        }
    }
}
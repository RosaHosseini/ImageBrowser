package com.rosahosseini.findr.feature.bookmark.viewmodel

import androidx.compose.runtime.Immutable
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentHashMapOf

object BookmarkContract {

    sealed interface Intent {
        data class OnUpdateBookmark(val photo: Photo, val enabled: Boolean) : Intent
    }

    @Immutable
    data class State(
        val bookmarks: ImmutableMap<String, Boolean> = persistentHashMapOf(),
        val photos: UiState<ImmutableList<Photo>> = UiState.Idle(null)
    ) {
        fun isBookmarked(photoId: String) = bookmarks[photoId] ?: false
    }
}

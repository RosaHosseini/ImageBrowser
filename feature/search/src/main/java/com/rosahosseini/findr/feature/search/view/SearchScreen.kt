package com.rosahosseini.findr.feature.search.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.feature.search.view.components.SearchTopAppBar
import com.rosahosseini.findr.feature.search.view.preview.SearchScreenPreviewProvider
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.PhotoCard
import com.rosahosseini.findr.ui.component.pullrefresh.PullToRefreshBox
import com.rosahosseini.findr.ui.component.state.ErrorComponent
import com.rosahosseini.findr.ui.component.state.LoadingComponent
import com.rosahosseini.findr.ui.extensions.OnBottomReached
import com.rosahosseini.findr.ui.extensions.localMessage
import com.rosahosseini.findr.ui.state.PagingState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
@Composable
internal fun SearchScreen(
    searchState: SearchContract.State,
    onPhotoClick: (Photo) -> Unit,
    onItemBookmarkClick: (Photo) -> Unit,
    onBookmarksClick: () -> Unit,
    onRemoveSuggestion: (String) -> Unit,
    onTermChange: (String) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    isBookmarked: (photoId: String) -> Boolean
) {
    val gridState = rememberLazyGridState()
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = searchState.photos.refreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = Modifier
    ) {
        Scaffold(
            topBar = {
                SearchTopAppBar(
                    term = searchState.term,
                    onBookmarksClick = onBookmarksClick,
                    suggestions = searchState.suggestions,
                    onTermChange = onTermChange,
                    onRemoveSuggestion = onRemoveSuggestion,
                    modifier = Modifier
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            val photosState = searchState.photos
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                photoItems(
                    photos = photosState.data,
                    isBookmarked = isBookmarked,
                    onBookmarkClick = onItemBookmarkClick,
                    onItemClick = onPhotoClick
                )
                when (photosState.status) {
                    PagingState.Status.Failure ->
                        photosState.throwable?.let { errorItem(it, onRetry) }

                    PagingState.Status.Loading ->
                        loadingItem()

                    else -> {}
                }
            }
        }
    }

    gridState.OnBottomReached(buffer = 2) {
        onLoadMore()
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.photoItems(
    photos: ImmutableList<Photo>,
    isBookmarked: (photoId: String) -> Boolean,
    onBookmarkClick: (Photo) -> Unit,
    onItemClick: (Photo) -> Unit
) {
    items(photos, contentType = { "image-card" }) { item ->
        PhotoCard(
            photo = item,
            isBookmarked = isBookmarked(item.id),
            onBookmarkClick = { onBookmarkClick(item) },
            modifier = Modifier
                .animateItemPlacement()
                .padding(Dimensions.xSmall)
                .clickable { onItemClick(item) }
        )
    }
}

private fun LazyGridScope.loadingItem() {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = "loading"
    ) {
        LoadingComponent(
            modifier = Modifier.padding(Dimensions.xxLarge)
        )
    }
}

private fun LazyGridScope.errorItem(throwable: Throwable, onRetryClick: () -> Unit) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = "error"
    ) {
        ErrorComponent(
            message = throwable.localMessage,
            onActionClick = onRetryClick,
            modifier = Modifier.padding(Dimensions.xxLarge)
        )
    }
}

@Composable
@Preview
private fun SearchScreenPreview(
    @PreviewParameter(SearchScreenPreviewProvider::class) state: SearchContract.State
) {
    FindrTheme {
        SearchScreen(
            searchState = state,
            onPhotoClick = {},
            onItemBookmarkClick = {},
            onBookmarksClick = {},
            onRemoveSuggestion = {},
            onTermChange = {},
            onLoadMore = {},
            isBookmarked = { false },
            onRefresh = {},
            onRetry = {}
        )
    }
}

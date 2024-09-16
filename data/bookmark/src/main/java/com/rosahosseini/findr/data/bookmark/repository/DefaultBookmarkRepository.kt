package com.rosahosseini.findr.data.bookmark.repository

import com.rosahosseini.findr.data.bookmark.local.BookmarkLocalDataSource
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.toPhoto
import com.rosahosseini.findr.db.entity.toPhotoEntity
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import com.rosahosseini.findr.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class DefaultBookmarkRepository(
    private val bookmarkLocalDataSource: BookmarkLocalDataSource,
    private val coroutineDispatchers: CoroutineDispatchers
) : BookmarkRepository {

    override suspend fun changeBookmarkState(
        photo: Photo,
        enabled: Boolean
    ): Result<Unit> {
        return runCatching {
            bookmarkLocalDataSource.changeBookmarkState(
                photo.toPhotoEntity(isBookmarked = enabled)
            )
        }
    }

    override fun getAllBookmarkedPhotos(): Flow<List<Photo>> {
        return bookmarkLocalDataSource.getBookmarkedPhotos()
            .map { it.map(PhotoEntity::toPhoto) }
            .flowOn(coroutineDispatchers.default)
    }
}

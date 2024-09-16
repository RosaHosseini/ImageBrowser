package com.rosahosseini.findr.data.bookmark.local

import com.rosahosseini.findr.db.dao.PhotoDao
import com.rosahosseini.findr.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

internal class BookmarkLocalDataSource(private val photoDao: PhotoDao) {
    suspend fun changeBookmarkState(photo: PhotoEntity) {
        photoDao.updateOrRemoveIfNotBookmark(photo)
    }

    fun getBookmarkedPhotos(): Flow<List<PhotoEntity>> {
        return photoDao.getBookmarkedFlow()
    }
}

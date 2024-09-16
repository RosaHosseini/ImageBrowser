package com.rosahosseini.findr.feature.search.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rosahosseini.findr.domain.search.SearchRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ClearCacheWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val searchRepository by inject<SearchRepository>()

    override suspend fun doWork(): Result {
        return try {
            searchRepository.clearExpiredData(EXPIRATION_TIME_MILLIS)
            Result.success()
        } catch (ignored: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val EXPIRATION_TIME_MILLIS = 7 * 24 * 60 * 60 * 1000L
    }
}

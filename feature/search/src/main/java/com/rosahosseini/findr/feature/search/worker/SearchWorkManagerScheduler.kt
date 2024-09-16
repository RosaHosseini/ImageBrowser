package com.rosahosseini.findr.feature.search.worker

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchWorkManagerScheduler(
    private val context: Context,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    @OptIn(DelicateCoroutinesApi::class)
    fun scheduleClearCachePeriodically() {
        GlobalScope.launch(coroutineDispatchers.io) {
            if (hasClearCacheScheduled()) {
                return@launch
            }
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
            val worker = PeriodicWorkRequest.Builder(
                ClearCacheWorker::class.java,
                REPEAT_INTERVAL_DAYS,
                TimeUnit.DAYS
            ).setConstraints(constraints).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_CLEAR_CACHE,
                ExistingPeriodicWorkPolicy.REPLACE,
                worker
            )
        }
    }

    @WorkerThread
    fun hasClearCacheScheduled(): Boolean {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork(TAG_CLEAR_CACHE)
            .get()
            .all { it.state.isFinished }
            .not()
    }

    companion object {

        private const val TAG_CLEAR_CACHE = "update_apps"
        private const val REPEAT_INTERVAL_DAYS = 1L
    }
}

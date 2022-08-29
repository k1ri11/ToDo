package com.example.todo.workmanager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.todo.R
import com.example.todo.data.datasource.network.RetrofitInstance
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ToDoWorkManager(
    val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        startForegroundService()
        val response = RetrofitInstance.getToDoApi().getAllTasks()
        if (response.isSuccessful) {

            return Result.success()
        }
        return Result.failure()
    }

    private suspend fun startForegroundService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "updating_task_channel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("updating_tasks")
                    .build()
            )
        )
    }

    companion object {
        private const val PERIOD_IN_HOURS = 8L
        private const val FLEX_TIME_PERIOD_IN_MINUTES = 25L

        private fun getConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }

        fun createPeriodicRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<ToDoWorkManager>(
                repeatInterval = PERIOD_IN_HOURS,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = FLEX_TIME_PERIOD_IN_MINUTES,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            )
                .setConstraints(getConstraints())
                .build()
        }

        private const val WORK_NAME = "todo_workManager"
        fun Context.startWorker() {

            val workManager = WorkManager.getInstance(this)
            val request = createPeriodicRequest()
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
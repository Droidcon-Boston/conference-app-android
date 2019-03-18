package com.mentalmachines.droidcon_boston.domain

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Didn't wanted to add dependency for thread management like Rx or coroutine only for twitter
 * feed feature hence this implementation of thread pool executor.
 * Stolen from google IO app and removed unnecessary stuff ;)
 * Check more about it here
 * https://github.com/google/iosched/blob/master/shared/src/main/java/com/google/samples/apps/iosched/shared/domain/internal/TaskScheduler.kt
 */
private const val NUMBER_OF_THREADS = 4

interface Scheduler {
    fun execute(task: () -> Unit)
}

object AsyncScheduler : Scheduler {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    override fun execute(task: () -> Unit) {
        executorService.execute(task)
    }

}

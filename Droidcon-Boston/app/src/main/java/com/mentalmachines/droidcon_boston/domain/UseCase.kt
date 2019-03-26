package com.mentalmachines.droidcon_boston.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mentalmachines.droidcon_boston.modal.Result
import timber.log.Timber

/**
 * Executes business logic asynchronously using a [AsyncScheduler].
 */
abstract class UseCase<in P, R> {

    val taskScheduler = AsyncScheduler

    /** Executes the use case asynchronously and places the [Result] in a MutableLiveData
     *
     * @param parameters the input parameters to run the use case with
     * @param result the MutableLiveData where the result is posted to
     *
     */
    operator fun invoke(parameters: P, result: MutableLiveData<Result<R>>): LiveData<Result<R>> {
        try {
            result.postValue(Result.Loading)
            taskScheduler.execute {
                try {
                    execute(parameters).let { useCaseResult ->
                        result.postValue(Result.Data(useCaseResult))
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    result.postValue(Result.Error(e.message ?: "Error"))
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            result.postValue(Result.Error(e.message ?: "Error"))
        }
        return result
    }

    /** Executes the use case asynchronously and returns a [Result] in a new LiveData object.
     *
     * @return an observable [LiveData] with a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    operator fun invoke(parameters: P): LiveData<Result<R>> {
        val liveCallback: MutableLiveData<Result<R>> = MutableLiveData()
        this(parameters, liveCallback)
        return liveCallback
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}

package kz.zhb.network.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kz.zhb.network.api.AsyncResult
import kz.zhb.network.api.RequestProcessor
import retrofit2.HttpException
import retrofit2.Response

internal class RequestProcessorImpl : RequestProcessor {

    override fun <T: Any> runInFlow(request: suspend () -> Response<T>) = flow {
        emit(handleResult(request()))
    }.catch { emit(handleException(it)) }

    private fun <T: Any> handleResult(result: Response<T>): AsyncResult<T> {
        return try {
            if (result.isSuccessful) {
                AsyncResult.Success(result.body() ?: Unit as T)
            } else {
                throw HttpException(result)
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun <T: Any> handleException(e: Throwable): AsyncResult<T> {
        return AsyncResult.Failure(e.message ?: "Unknown failure message")
    }
}
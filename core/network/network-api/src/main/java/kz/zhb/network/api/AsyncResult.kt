package kz.zhb.network.api

sealed class AsyncResult<out T : Any> {
    class Success<out T : Any>(val data: T) : AsyncResult<T>()
    class Failure(val message: String) : AsyncResult<Nothing>()
}
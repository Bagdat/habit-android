package kz.zhb.network.api

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RequestProcessor {
    /** runInFlow { api.getSomething() } — запрос выполняется при сборе Flow, ошибки превращаются в Failure. */
    fun <T : Any> runInFlow(request: suspend () -> Response<T>): Flow<AsyncResult<T>>
}

package kz.zhb.network.api

import retrofit2.converter.gson.GsonConverterFactory

interface ApiCreator {
    fun <T> create(
        api: Class<T>,
        baseUrl: String,
        converterFactory: GsonConverterFactory? = null,
        authEnable: Boolean = true,
        mockEnable: Boolean = false
    ): T
}
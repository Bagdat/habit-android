package kz.zhb.network.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.zhb.network.api.ApiCreator
import kz.zhb.network.api.RequestProcessor
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {

    single { GsonBuilder().create() }

    single {
        GsonConverterFactory.create(get<Gson>())
    } bind Converter.Factory::class

    factory<RequestProcessor> { RequestProcessorImpl() }

    factory { HttpLoggingInterceptor() }

    factory { (authEnable: Boolean, mockEnable: Boolean) ->
        OkHttpClient.Builder()
            .apply {
                connectTimeout(60L, TimeUnit.SECONDS)
                readTimeout(60L, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                dns(Dns.SYSTEM)
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = get<HttpLoggingInterceptor>()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(interceptor = loggingInterceptor)
                }
            }
            .build()
    }

    factory { (baseUrl: String, authEnable: Boolean, mockEnable: Boolean) ->
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get<OkHttpClient> { parametersOf(authEnable, mockEnable) })
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    factory {
        object : ApiCreator {
            override fun <T> create(
                api: Class<T>,
                baseUrl: String,
                converterFactory: GsonConverterFactory?,
                authEnable: Boolean,
                mockEnable: Boolean
            ): T {
                return get<Retrofit>(
                    parameters = { parametersOf(baseUrl, authEnable, mockEnable) }
                ).create(api)
            }
        }
    } bind ApiCreator::class
}
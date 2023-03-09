package com.android.justordinarymovieapp.di

import android.content.Context
import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.utils.Constants
import com.android.justordinarymovieapp.utils.JNIUtil
import com.android.justordinarymovieapp.utils.NetworkHelper
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkModule {

    val modules = module {
        single { provideRetrofit(get()) }
        single { provideOkHttpClient(androidContext()) }
        single { provideAPI(get()) }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(JNIUtil.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        val chuckCollector = ChuckerCollector(
            context,
            retentionPeriod = RetentionManager.Period.ONE_DAY
        )
        val chuckerInterceptor =
            ChuckerInterceptor.Builder(context)
                .collector(chuckCollector)
                .maxContentLength(250_000L)
                .alwaysReadResponseBody(true)
                .build()

        val connectivityInterceptor = Interceptor { chain ->
            if (NetworkHelper.isInternetAvailable()) {
                chain.proceed(chain.request())
            } else {
                throw IOException("Cannot connect to server: Please make sure you are connected to the Internet and try again.")
            }
        }

        val customHeaderInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter(Constants.API_KEY_NAME, JNIUtil.apiKey)
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            chain.proceed(requestBuilder.build())
        }

        return OkHttpClient().newBuilder()
            .connectTimeout(30.toLong(), TimeUnit.SECONDS)
            .writeTimeout(30.toLong(), TimeUnit.SECONDS)
            .readTimeout(30.toLong(), TimeUnit.SECONDS)
            .addInterceptor(customHeaderInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(connectivityInterceptor)
            .build()
    }

    private fun provideAPI(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}
package com.android.justordinarymovieapp.di

import com.android.justordinarymovieapp.JustAnotherMovieApp
import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.utils.JNIUtil
import com.android.justordinarymovieapp.utils.NetworkHelper
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkModule {

    val modules = module {
        single { provideRetrofit(get()) }
        single { provideOkHttpClient() }
        single { provideAPI(get()) }
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(JNIUtil.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val chuckCollector = ChuckerCollector(
            JustAnotherMovieApp.instance.applicationContext,
            retentionPeriod = RetentionManager.Period.ONE_DAY
        )
        val chuckerInterceptor =
            ChuckerInterceptor.Builder(JustAnotherMovieApp.instance.applicationContext)
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

        return OkHttpClient().newBuilder()
            .connectTimeout(30.toLong(), TimeUnit.SECONDS)
            .writeTimeout(30.toLong(), TimeUnit.SECONDS)
            .readTimeout(30.toLong(), TimeUnit.SECONDS)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(connectivityInterceptor)
            .build()
    }

    private fun provideAPI(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}
package com.android.justordinarymovieapp.di

import com.android.justordinarymovieapp.data.repository.MovieRepository
import com.android.justordinarymovieapp.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

object RepositoryModule {

    val modules = module {
        single<MovieRepository> { MovieRepositoryImpl(get()) }
    }

}
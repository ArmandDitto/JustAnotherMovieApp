package com.android.justordinarymovieapp.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object Module {

    private val appModules = module {
        single { Dispatchers.IO }
    }

    fun getAll() = listOf(
        appModules,
        NetworkModule.modules,
        RepositoryModule.modules,
        ViewModelModule.modules
    )

}
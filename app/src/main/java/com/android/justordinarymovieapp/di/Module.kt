package com.android.justordinarymovieapp.di

import androidx.room.Room
import com.android.justordinarymovieapp.data.room.AppDataBase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object Module {

    private val appModules = module {
        single { Dispatchers.IO }
    }

    private val databaseModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_room_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    fun getAll() = listOf(
        appModules,
        databaseModule,
        DaoModule.modules,
        RepositoryModule.modules,
        ViewModelModule.modules,
        NetworkModule.modules,
    )

}
package com.android.justordinarymovieapp.di

import com.android.justordinarymovieapp.data.room.RoomDao
import org.koin.dsl.module

object DaoModule {

    val modules = module {
        single { RoomDao(get()).genreDao() }
    }

}
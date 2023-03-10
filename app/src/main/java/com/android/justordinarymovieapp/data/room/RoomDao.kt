package com.android.justordinarymovieapp.data.room

import com.android.justordinarymovieapp.data.dao.GenreDao

class RoomDao(private val appDataBase: AppDataBase) {

    fun genreDao(): GenreDao = appDataBase.genreDao

}
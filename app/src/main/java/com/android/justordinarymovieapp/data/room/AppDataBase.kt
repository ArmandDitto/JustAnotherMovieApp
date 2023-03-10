package com.android.justordinarymovieapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.justordinarymovieapp.data.dao.GenreDao
import com.android.justordinarymovieapp.data.entity.GenreEntity

@Database(
    entities = [
        GenreEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDataBase : RoomDatabase() {

    abstract val genreDao: GenreDao

}
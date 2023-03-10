package com.android.justordinarymovieapp.data.dao

import androidx.room.*
import com.android.justordinarymovieapp.data.entity.GenreEntity

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: GenreEntity)

    @Query("SELECT * FROM ${GenreEntity.TABLE_NAME}")
    fun getAllData(): List<GenreEntity>

    @Query("DELETE FROM ${GenreEntity.TABLE_NAME}")
    suspend fun clearData()

}
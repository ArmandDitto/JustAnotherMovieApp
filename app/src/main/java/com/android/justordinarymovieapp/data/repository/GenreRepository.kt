package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.data.entity.GenreEntity
import com.android.justordinarymovieapp.model.genre.GenreResponse

interface GenreRepository {

    suspend fun getAllGenres(): ResultWrapper<GenreResponse>

    suspend fun getAllGenreLocal(): List<GenreEntity>

}
package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.genre.GenreResponse

interface MovieRepository {

    suspend fun getMovieListAsync(
        page: Int,
    ): PagingWrapper<List<MovieResponse>>

    suspend fun getMoviesByGenre(
        page: Int,
        genreId: Int,
    ): PagingWrapper<List<MovieResponse>>

    suspend fun getAllGenres(): ResultWrapper<GenreResponse>

}
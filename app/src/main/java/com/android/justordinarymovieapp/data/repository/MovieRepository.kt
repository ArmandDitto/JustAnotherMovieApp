package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.model.MovieResponse

interface MovieRepository {

    suspend fun getMovieListAsync(
        page: Int,
    ): PagingWrapper<List<MovieResponse>>

}
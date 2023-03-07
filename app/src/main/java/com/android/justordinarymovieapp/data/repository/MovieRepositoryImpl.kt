package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.utils.JNIUtil

class MovieRepositoryImpl(
    private val api: ApiService
) : MovieRepository {

    override suspend fun getMovieListAsync(
        page: Int
    ): PagingWrapper<List<MovieResponse>> {
        return api.getPopularMovies(page, JNIUtil.apiKey)
    }


}
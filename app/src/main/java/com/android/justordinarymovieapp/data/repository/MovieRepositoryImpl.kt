package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.base.network.safeApiCall
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.genre.GenreResponse
import com.android.justordinarymovieapp.utils.JNIUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MovieRepositoryImpl(
    private val api: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override suspend fun getMovieListAsync(
        page: Int
    ): PagingWrapper<List<MovieResponse>> {
        return api.getPopularMovies(page, JNIUtil.apiKey)
    }

    override suspend fun getMoviesByGenre(
        page: Int,
        genreId: Int
    ): PagingWrapper<List<MovieResponse>> {
        return api.getMoviesByGenre(JNIUtil.apiKey, genreId, page)
    }

    override suspend fun getAllGenres(): ResultWrapper<GenreResponse> {
        return safeApiCall(dispatcher) { api.getAllGenres(JNIUtil.apiKey) }
    }


}
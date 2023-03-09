package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.base.network.safeApiCall
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.genre.GenreResponse
import com.android.justordinarymovieapp.model.review.Review
import com.android.justordinarymovieapp.model.video.Video
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MovieRepositoryImpl(
    private val api: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override suspend fun getAllGenres(): ResultWrapper<GenreResponse> {
        return safeApiCall(dispatcher) { api.getAllGenres() }
    }

    override suspend fun getMoviesByGenre(
        page: Int,
        genreId: Int
    ): PagingWrapper<List<MovieResponse>> {
        return api.getMoviesByGenre(genreId, page)
    }

    override suspend fun getMovieDetails(movieId: Int): ResultWrapper<MovieResponse> {
        return safeApiCall(dispatcher) { api.getMovieDetails(movieId) }
    }

    override suspend fun getReview(
        movieId: Int,
        page: Int
    ): ResultWrapper<PagingWrapper<List<Review>>> {
        return safeApiCall(dispatcher) { api.getReview(movieId, page) }
    }

    override suspend fun getReviewPaging(movieId: Int, page: Int): PagingWrapper<List<Review>> {
        return api.getReview(movieId, page)
    }

    override suspend fun getMovieVideos(movieId: Int): ResultWrapper<PagingWrapper<List<Video>>> {
        return safeApiCall(dispatcher) {api.getMovieVideos(movieId)}
    }

}
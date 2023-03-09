package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.genre.GenreResponse
import com.android.justordinarymovieapp.model.review.Review
import com.android.justordinarymovieapp.model.video.Video

interface MovieRepository {

    suspend fun getMovieListAsync(
        page: Int,
    ): PagingWrapper<List<MovieResponse>>

    suspend fun getMoviesByGenre(
        page: Int,
        genreId: Int,
    ): PagingWrapper<List<MovieResponse>>

    suspend fun getAllGenres(): ResultWrapper<GenreResponse>

    suspend fun getMovieDetails(
        movieId: Int,
    ): ResultWrapper<MovieResponse>

    suspend fun getReview(
        movieId: Int,
        page: Int,
    ): ResultWrapper<PagingWrapper<List<Review>>>

    suspend fun getReviewPaging(
        movieId: Int,
        page: Int,
    ): PagingWrapper<List<Review>>

    suspend fun getMovieVideos(
        movieId: Int
    ): ResultWrapper<PagingWrapper<List<Video>>>
}
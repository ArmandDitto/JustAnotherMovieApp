package com.android.justordinarymovieapp.base.network

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.genre.GenreResponse
import com.android.justordinarymovieapp.model.review.Review
import com.android.justordinarymovieapp.model.video.Video
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): PagingWrapper<List<MovieResponse>>

    @GET("genre/movie/list")
    suspend fun getAllGenres(
        @Query("api_key") apiKey: String
    ): GenreResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
    ): PagingWrapper<List<MovieResponse>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReview(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): PagingWrapper<List<Review>>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): PagingWrapper<List<Video>>

}
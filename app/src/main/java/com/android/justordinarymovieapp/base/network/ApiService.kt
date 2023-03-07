package com.android.justordinarymovieapp.base.network

import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): PagingWrapper<List<MovieResponse>>

}
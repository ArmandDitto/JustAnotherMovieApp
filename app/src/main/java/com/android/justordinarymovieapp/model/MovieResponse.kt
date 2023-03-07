package com.android.justordinarymovieapp.model


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("title")
    val title: String? = null
)
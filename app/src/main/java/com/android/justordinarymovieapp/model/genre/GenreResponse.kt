package com.android.justordinarymovieapp.model.genre


import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = listOf()
)
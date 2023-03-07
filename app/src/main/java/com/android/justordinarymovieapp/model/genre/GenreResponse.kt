package com.android.justordinarymovieapp.model.genre


import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = listOf()
) {

    data class Genre(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null
    )

}
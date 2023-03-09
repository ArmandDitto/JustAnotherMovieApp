package com.android.justordinarymovieapp.model.review

import com.google.gson.annotations.SerializedName


class AuthorDetails(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("avatar_path")
    val avatarPath: String? = null,

    @SerializedName("rating")
    val rating: Float? = null,

)
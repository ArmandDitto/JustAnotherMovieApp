package com.android.justordinarymovieapp.base.model

import com.google.gson.annotations.SerializedName

data class PagingWrapper<T>(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: T? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)
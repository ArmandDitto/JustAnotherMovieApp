package com.android.justordinarymovieapp.base.paging

sealed class PagingUiModel<out T> {

    data class DataItem<out T>(val data: T) : PagingUiModel<T>()
    data class SeparatorItem(val description: String) : PagingUiModel<Nothing>()

}
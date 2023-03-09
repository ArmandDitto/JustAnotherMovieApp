package com.android.justordinarymovieapp.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.android.justordinarymovieapp.base.model.PagingWrapper
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.base.paging.PagingUiModel
import com.android.justordinarymovieapp.data.paging.ReviewPagingSource
import com.android.justordinarymovieapp.data.repository.MovieRepository
import com.android.justordinarymovieapp.model.review.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val repository : MovieRepository
) : ViewModel() {

    private val _reviewPagingLiveData =
        MutableLiveData<PagingData<PagingUiModel<Review>>>()
    val reviewPagingLiveData: LiveData<PagingData<PagingUiModel<Review>>> =
        _reviewPagingLiveData

    private val _reviewLiveData = MutableLiveData<ResultWrapper<PagingWrapper<List<Review>>>>()
    val reviewLiveData: LiveData<ResultWrapper<PagingWrapper<List<Review>>>> = _reviewLiveData

    fun fetchReviewsPaging(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(PagingConfig(pageSize = 10)) {
                ReviewPagingSource(repository, movieId)
            }.flow
                .map { pagingData ->
                    pagingData.map { PagingUiModel.DataItem(it) }
                }
                .map {
                    it.insertSeparators { before, after ->
                        if (before != null && after == null) {
                            PagingUiModel.SeparatorItem(
                                ""
                            )
                        } else {
                            return@insertSeparators null
                        }
                    }
                }
                .cachedIn(viewModelScope).collectLatest {
                    _reviewPagingLiveData.postValue(it)
                }
        }
    }

    fun fetchReviews(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _reviewLiveData.postValue(ResultWrapper.Loading)
            _reviewLiveData.postValue(repository.getReview(movieId, 1))
        }
    }

}
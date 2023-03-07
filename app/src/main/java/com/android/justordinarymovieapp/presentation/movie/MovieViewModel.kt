package com.android.justordinarymovieapp.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.data.paging.MoviePagingSource
import com.android.justordinarymovieapp.data.repository.MovieRepository
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.base.paging.PagingUiModel
import com.android.justordinarymovieapp.data.paging.MovieByGenrePagingSource
import com.android.justordinarymovieapp.model.genre.GenreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieViewModel(
    private val repository :MovieRepository
) : ViewModel() {

    private val _moviePagingLiveData =
        MutableLiveData<PagingData<PagingUiModel<MovieResponse>>>()
    val moviePagingLiveData: LiveData<PagingData<PagingUiModel<MovieResponse>>> =
        _moviePagingLiveData

    private val _movieDetailLiveData = MutableLiveData<ResultWrapper<MovieResponse>>()
    val movieDetailLiveData: LiveData<ResultWrapper<MovieResponse>> = _movieDetailLiveData

    fun fetchMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(PagingConfig(pageSize = 10)) {
                MoviePagingSource(repository)
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
                    _moviePagingLiveData.postValue(it)
                }
        }
    }

    fun fetchMoviesByGenre(genreId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(PagingConfig(pageSize = 10)) {
                MovieByGenrePagingSource(repository, genreId)
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
                    _moviePagingLiveData.postValue(it)
                }
        }
    }

    fun fetchDetailMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetailLiveData.postValue(repository.getMovieDetails(movieId))
        }
    }

}
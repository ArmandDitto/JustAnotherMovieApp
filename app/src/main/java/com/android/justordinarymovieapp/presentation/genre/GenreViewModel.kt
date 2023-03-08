package com.android.justordinarymovieapp.presentation.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.data.repository.MovieRepository
import com.android.justordinarymovieapp.model.genre.GenreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _genresLiveData = MutableLiveData<ResultWrapper<GenreResponse>>()
    val genresLiveData: LiveData<ResultWrapper<GenreResponse>> = _genresLiveData

    init {
        fetchGenres()
    }

    fun fetchGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            _genresLiveData.postValue(ResultWrapper.Loading)
            _genresLiveData.postValue(repository.getAllGenres())
        }
    }

}
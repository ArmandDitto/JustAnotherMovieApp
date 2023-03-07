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

    private val _genreLiveData = MutableLiveData<ResultWrapper<GenreResponse>>()
    val genreLiveData: LiveData<ResultWrapper<GenreResponse>> = _genreLiveData

    init {
        fetchGenres()
    }

    fun fetchGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            _genreLiveData.postValue(repository.getAllGenres())
        }
    }

}
package com.android.justordinarymovieapp.presentation.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.data.dao.GenreDao
import com.android.justordinarymovieapp.data.entity.GenreEntity
import com.android.justordinarymovieapp.data.repository.GenreRepository
import com.android.justordinarymovieapp.model.genre.Genre
import com.android.justordinarymovieapp.model.genre.GenreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreViewModel(
    private val repository: GenreRepository,
) : ViewModel() {

    private val _genresLiveData = MutableLiveData<ResultWrapper<GenreResponse>>()
    val genresLiveData: LiveData<ResultWrapper<GenreResponse>> = _genresLiveData

    private val _genreNameLiveData = MutableLiveData<String>()
    val genreNameLiveData: LiveData<String> = _genreNameLiveData

    fun fetchGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            _genresLiveData.postValue(ResultWrapper.Loading)
            _genresLiveData.postValue(repository.getAllGenres())
        }
    }

    fun getGenreName(genreId: Int) {
        var selectedGenre : GenreEntity? = null
        viewModelScope.launch(Dispatchers.IO) {
            selectedGenre = repository.getAllGenreLocal().find {
                it.id?.equals(genreId) == true
            }
            _genreNameLiveData.postValue(selectedGenre?.name ?: "Genre")
        }
    }

}
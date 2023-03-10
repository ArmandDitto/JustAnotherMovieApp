package com.android.justordinarymovieapp.data.repository

import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.base.network.ApiService
import com.android.justordinarymovieapp.base.network.safeApiCall
import com.android.justordinarymovieapp.data.dao.GenreDao
import com.android.justordinarymovieapp.data.entity.GenreEntity
//import com.android.justordinarymovieapp.data.dao.GenreDao
import com.android.justordinarymovieapp.model.genre.GenreResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GenreRepositoryImpl(
    private val api: ApiService,
    private val genreDao: GenreDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GenreRepository {

    override suspend fun getAllGenres(): ResultWrapper<GenreResponse> {
        val result = safeApiCall(dispatcher) { api.getAllGenres() }
        if (result is ResultWrapper.Success) {
            result.value.genres?.forEach {
                val genreData = GenreEntity.ModelMapper.fromGenre(it)
                genreDao.insertData(genreData)
            }
        }
        return result
    }

    override suspend fun getAllGenreLocal(): List<GenreEntity> {
        return genreDao.getAllData()
    }

}
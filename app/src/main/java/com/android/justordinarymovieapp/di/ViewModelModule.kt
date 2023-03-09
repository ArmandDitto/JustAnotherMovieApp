package com.android.justordinarymovieapp.di

import com.android.justordinarymovieapp.presentation.genre.GenreViewModel
import com.android.justordinarymovieapp.presentation.movie.MovieViewModel
import com.android.justordinarymovieapp.presentation.review.ReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    val modules = module {
        viewModel { MovieViewModel(get()) }
        viewModel { GenreViewModel(get()) }
        viewModel { ReviewViewModel(get()) }
    }

}
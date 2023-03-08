package com.android.justordinarymovieapp.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.FragmentMovieDetailBinding
import com.android.justordinarymovieapp.model.MovieResponse
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val viewModel: MovieViewModel by viewModel()

    private val movieId by lazy { arguments?.getInt(KEY_MOVIE_ID, 0) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieDetailBinding
        get() = FragmentMovieDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObserver()
        movieId?.let { viewModel.fetchDetailMovie(it) }
    }

    private fun setUpObserver() {
        viewModel.movieDetailLiveData.observe(viewLifecycleOwner) {
            when(it) {
                ResultWrapper.Loading -> {

                }
                is ResultWrapper.Error -> {

                }
                is ResultWrapper.Success -> {
                    onDetailMovieSuccess(it.value)
                }
            }
        }
    }

    private fun onDetailMovieSuccess(data : MovieResponse) {
        binding.collapsingToolBarLayout.apply {
            title = data.title
            setCollapsedTitleTextColor(context.getColor(R.color.text_android_white_default))
            setExpandedTitleColor(context.getColor(R.color.text_android_white_default))
        }

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original/${data.backdropPath}")
            .into(binding.imgBackdrop)
    }

    companion object {

        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

    }
}
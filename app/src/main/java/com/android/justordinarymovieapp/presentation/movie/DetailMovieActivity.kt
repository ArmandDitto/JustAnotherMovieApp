package com.android.justordinarymovieapp.presentation.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.ActivityDetailMovieBinding
import com.android.justordinarymovieapp.model.MovieResponse
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieActivity : BaseActivity<ActivityDetailMovieBinding>() {

    private val viewModel: MovieViewModel by viewModel()

    private val movieId: Int by lazy { intent.getIntExtra(KEY_MOVIE_ID, 0) }

    override val bindingInflater: (LayoutInflater) -> ActivityDetailMovieBinding
        get() = ActivityDetailMovieBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpObserver()
        binding.toolbar.apply {
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        viewModel.fetchDetailMovie(movieId)
    }

    private fun setUpObserver() {
        viewModel.movieDetailLiveData.observe(this) {
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
            setCollapsedTitleTextColor(getColor(R.color.text_android_white_default))
            setExpandedTitleColor(getColor(R.color.text_android_white_default))
        }

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original/${data.backdropPath}")
            .into(binding.imgBackdrop)
    }

    companion object {

        private const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun launchIntent(context: Context, movieId: Int) {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra(KEY_MOVIE_ID, movieId)
            context.startActivity(intent)
        }
    }
}
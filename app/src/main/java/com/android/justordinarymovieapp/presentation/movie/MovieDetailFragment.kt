package com.android.justordinarymovieapp.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.FragmentMovieDetail2Binding
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.presentation.review.ReviewAdapter
import com.android.justordinarymovieapp.presentation.review.ReviewViewModel
import com.android.justordinarymovieapp.utils.goneView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : BaseFragment<FragmentMovieDetail2Binding>() {

    private val movieViewModel: MovieViewModel by viewModel()
    private val reviewViewModel: ReviewViewModel by viewModel()

    private val movieId by lazy { arguments?.getInt(KEY_MOVIE_ID, 0) }

    private lateinit var reviewAdapter: ReviewAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieDetail2Binding
        get() = FragmentMovieDetail2Binding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setupListener()
        setUpObserver()
        movieId?.let { movieViewModel.fetchDetailMovie(it) }
        reviewViewModel.fetchReviews(28)
        movieId?.let { movieViewModel.fetchMovieVideos(it) }

    }

    private fun setUpView() {
        binding.tvSeeAllReview.setOnClickListener {
            val args = Bundle()
            movieId?.let { id -> args.putInt(MovieListFragment.KEY_MOVIE_ID, id) }
            findNavController().navigate(R.id.action_movieDetailFragment_to_reviewListFragment, args)
        }

        reviewAdapter = ReviewAdapter().apply {

        }

        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = reviewAdapter
        }
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            movieId?.let { movieViewModel.fetchMovieVideos(it) }
        }
    }

    private fun setUpYoutubePlayer(videoId: String?) {
        if (videoId != null) {
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        } else {
            binding.layoutTrailer.goneView()
        }
    }

    private fun setUpObserver() {
        movieViewModel.movieDetailLiveData.observe(viewLifecycleOwner) {
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
        movieViewModel.movieVideosLiveData.observe(viewLifecycleOwner) {
            when(it) {
                ResultWrapper.Loading -> {

                }
                is ResultWrapper.Error -> {

                }
                is ResultWrapper.Success -> {
                    val selectedVideo = it.value.results?.find { video ->
                        video.type.equals("Trailer")
                    }
                    if (selectedVideo != null) {
                        selectedVideo.key?.let { youtubeId -> setUpYoutubePlayer(youtubeId) }
                    }
                }
            }
        }
        reviewViewModel.reviewLiveData.observe(viewLifecycleOwner) {
            when(it) {
                ResultWrapper.Loading -> {

                }
                is ResultWrapper.Error -> {

                }
                is ResultWrapper.Success -> {
                    it.value.results?.take(1).let { result ->
                        if (result != null) {
                            reviewAdapter.addItems(result)
                        }
                    }
                }
            }
        }
    }

    private fun onDetailMovieSuccess(data : MovieResponse) {

        binding.tvMovieTitle.text = data.title
        binding.tvMovieReleaseDate.text = data.releaseDate
        binding.tvMovieOverview.text = data.overview
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original/${data.posterPath}")
            .into(binding.ivMoviePoster)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original/${data.backdropPath}")
            .into(binding.ivMovieBackdrop)
    }

    companion object {

        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

    }
}
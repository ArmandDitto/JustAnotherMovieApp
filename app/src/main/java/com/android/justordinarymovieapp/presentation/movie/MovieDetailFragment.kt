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
import com.android.justordinarymovieapp.base.view.loading.ProgressDialog
import com.android.justordinarymovieapp.databinding.FragmentMovieDetailBinding
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.model.review.Review
import com.android.justordinarymovieapp.presentation.review.ReviewAdapter
import com.android.justordinarymovieapp.presentation.review.ReviewViewModel
import com.android.justordinarymovieapp.utils.Constants
import com.android.justordinarymovieapp.utils.goneView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val movieViewModel: MovieViewModel by viewModel()
    private val reviewViewModel: ReviewViewModel by viewModel()

    private val movieId by lazy { arguments?.getInt(Constants.KEY_MOVIE_ID_BUNDLE, 0) }

    private lateinit var reviewAdapter: ReviewAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieDetailBinding
        get() = FragmentMovieDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setupListener()
        setUpObserver()
        fetchMovieDetail()
        fetchMovieReview()
        fetchMovieVideo()

    }

    private fun setUpView() {
        binding.tvSeeAllReview.setOnClickListener {
            val args = Bundle()
            movieId?.let { id -> args.putInt(Constants.KEY_MOVIE_ID_BUNDLE, id) }
            findNavController().navigate(
                R.id.action_movieDetailFragment_to_reviewListFragment, args
            )
        }

        reviewAdapter = ReviewAdapter().apply {}

        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = reviewAdapter
        }
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            fetchMovieDetail()
            fetchMovieVideo()
            fetchMovieReview()
        }
    }

    private fun setUpObserver() {
        movieViewModel.movieDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    ProgressDialog.show(requireContext())
                }

                is ResultWrapper.Error -> {
                    ProgressDialog.dismiss()
                    showErrorDialog(
                        onPositiveBtnClick = { fetchMovieDetail() },
                        isCancelable = true
                    )
                }

                is ResultWrapper.Success -> {
                    ProgressDialog.dismiss()
                    onGetDetailMovieSuccess(it.value)
                }
            }
        }
        movieViewModel.movieVideosLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    ProgressDialog.show(requireContext())
                }

                is ResultWrapper.Error -> {
                    ProgressDialog.dismiss()
                }

                is ResultWrapper.Success -> {
                    ProgressDialog.dismiss()
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
            when (it) {
                is ResultWrapper.Loading -> {
                    ProgressDialog.show(requireContext())
                }

                is ResultWrapper.Error -> {
                    ProgressDialog.dismiss()
                }

                is ResultWrapper.Success -> {
                    ProgressDialog.dismiss()
                    it.value.results?.let { reviewList -> onGetReviewMovieSuccess(reviewList) }
                }
            }
        }
    }

    private fun setUpYoutubePlayer(videoId: String?) {
        if (videoId != null) {
            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        } else {
            binding.layoutTrailer.goneView()
        }
    }

    private fun fetchMovieDetail() {
        movieId?.let { movieViewModel.fetchDetailMovie(it) }
    }

    private fun fetchMovieReview() {
        movieId?.let { reviewViewModel.fetchReviews(it) }
    }

    private fun fetchMovieVideo() {
        movieId?.let { movieViewModel.fetchMovieVideos(it) }
    }

    private fun onGetReviewMovieSuccess(data: List<Review>) {
        data.take(1).let { result ->
            if(result.isNotEmpty()) {
                reviewAdapter.addItems(result)
            } else {
                binding.layoutReview.goneView()
            }
        }
    }

    private fun onGetDetailMovieSuccess(data: MovieResponse) {
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
}
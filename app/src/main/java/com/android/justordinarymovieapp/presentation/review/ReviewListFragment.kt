package com.android.justordinarymovieapp.presentation.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.paging.PagingLoadStateAdapter
import com.android.justordinarymovieapp.databinding.FragmentReviewListBinding
import com.android.justordinarymovieapp.presentation.ContainerMovieActivity
import com.android.justordinarymovieapp.utils.Constants
import com.android.justordinarymovieapp.utils.setAutoNullAdapter
import com.kennyc.view.MultiStateView
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListFragment : BaseFragment<FragmentReviewListBinding>() {

    private val viewModel: ReviewViewModel by viewModel()
    private lateinit var reviewAdapter: ReviewPagingAdapter

    private val movieId by lazy { arguments?.getInt(Constants.KEY_MOVIE_ID_BUNDLE, 0) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReviewListBinding
        get() = FragmentReviewListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupView()
        setUpObserver()
        fetchData()

    }

    private fun setUpObserver() {
        viewModel.reviewPagingLiveData.observe(viewLifecycleOwner) {
            reviewAdapter.submitData(this.lifecycle, it)
        }
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            fetchData()
        }
    }

    private fun setupView() {
        (requireActivity() as ContainerMovieActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = getString(R.string.label_title_review_list)
        }

        reviewAdapter = ReviewPagingAdapter(requireContext()).apply {
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        if (this.itemCount == 0) {
                            binding.msvReview.viewState = MultiStateView.ViewState.LOADING
                        } else {
                            binding.msvReview.viewState = MultiStateView.ViewState.LOADING
                        }
                    }

                    is LoadState.NotLoading -> {
                        binding.msvReview.viewState = MultiStateView.ViewState.CONTENT
                    }

                    is LoadState.Error -> {
                        showErrorDialog(
                            onPositiveBtnClick = { fetchData() },
                            isCancelable = true
                        )
                    }
                }
            }
        }

        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setAutoNullAdapter(reviewAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter().apply {
                    onRetry = { reviewAdapter.refresh() }
                },
                footer = PagingLoadStateAdapter().apply {
                    onRetry = { reviewAdapter.retry() }
                }
            ))
        }
    }

    private fun fetchData() {
        movieId?.let { viewModel.fetchReviewsPaging(it) }
    }
}
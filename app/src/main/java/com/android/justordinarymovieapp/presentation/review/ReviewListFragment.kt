package com.android.justordinarymovieapp.presentation.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.paging.PagingLoadStateAdapter
import com.android.justordinarymovieapp.databinding.FragmentReviewListBinding
import com.android.justordinarymovieapp.utils.setAutoNullAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListFragment : BaseFragment<FragmentReviewListBinding>() {

    private val viewModel: ReviewViewModel by viewModel()
    private lateinit var reviewAdapter: ReviewPagingAdapter

    private val movieId by lazy { arguments?.getInt(KEY_MOVIE_ID, 0) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReviewListBinding
        get() = FragmentReviewListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupView()
        setUpObserver()
        movieId?.let { viewModel.fetchReviewsPaging(28) }
    }

    private fun setUpObserver() {
        viewModel.reviewPagingLiveData.observe(viewLifecycleOwner) {
            reviewAdapter.submitData(this.lifecycle, it)
        }

        this.lifecycleScope.launch {
            reviewAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
                binding.rvContent.isVisible = binding.progressBar.isVisible.not()
            }
        }
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupView() {
        reviewAdapter = ReviewPagingAdapter(requireContext()).apply {

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

    companion object {

        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

    }
}
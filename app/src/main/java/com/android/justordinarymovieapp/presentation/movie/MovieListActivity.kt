package com.android.justordinarymovieapp.presentation.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.base.paging.PagingLoadStateAdapter
import com.android.justordinarymovieapp.databinding.ActivityMovieListBinding
import com.android.justordinarymovieapp.utils.setAutoNullAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieListActivity : BaseActivity<ActivityMovieListBinding>() {

    private val viewModel: MovieViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupListener()
        setupView()
        setUpObserver()
        viewModel.fetchMovieList()

    }

    override val bindingInflater: (LayoutInflater) -> ActivityMovieListBinding
        get() = ActivityMovieListBinding::inflate

    private fun setUpObserver() {
        viewModel.moviePagingLiveData.observe(this) {
            movieAdapter.submitData(this.lifecycle, it)
        }

        this.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collectLatest {
                binding.progressBar.isVisible = it.refresh is LoadState.Loading
                binding.rvContent.isVisible = binding.progressBar.isVisible.not()
            }
        }
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            movieAdapter.refresh()
            viewModel.fetchMovieList()
        }
    }

    private fun setupView() {
        movieAdapter = MovieAdapter(this).apply {
            onRootClick = {
                it.id?.let { _ ->

                }
            }
        }

        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(context)
            setAutoNullAdapter(movieAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter().apply {
                    onRetry = { movieAdapter.refresh() }
                },
                footer = PagingLoadStateAdapter().apply {
                    onRetry = { movieAdapter.retry() }
                }
            ))
        }
    }

    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, MovieListActivity::class.java)
            context.startActivity(intent)
        }

    }

}
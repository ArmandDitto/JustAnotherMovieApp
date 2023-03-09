package com.android.justordinarymovieapp.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.paging.PagingLoadStateAdapter
import com.android.justordinarymovieapp.databinding.FragmentMovieListBinding
import com.android.justordinarymovieapp.presentation.ContainerMovieActivity
import com.android.justordinarymovieapp.utils.Constants
import com.android.justordinarymovieapp.utils.setAutoNullAdapter
import com.kennyc.view.MultiStateView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : BaseFragment<FragmentMovieListBinding>() {

    private val viewModel: MovieViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter

    private val genreId by lazy { arguments?.getInt(Constants.KEY_GENRE_ID_BUNDLE, 0) }
    private val genreName by lazy { arguments?.getString(Constants.KEY_GENRE_NAME_BUNDLE, "Genre") }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieListBinding
        get() = FragmentMovieListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupView()
        setUpObserver()
        fetchData()

    }

    private fun fetchData() {
        genreId?.let { viewModel.fetchMoviesByGenre(it) }
    }

    private fun setUpObserver() {
        viewModel.moviePagingLiveData.observe(viewLifecycleOwner) {
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
            fetchData()
        }
    }

    private fun setupView() {
        (requireActivity() as ContainerMovieActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = String.format(getString(R.string.label_title_movie_list), genreName)
        }

        movieAdapter = MovieAdapter(requireContext()).apply {
            onRootClick = {
                it.id?.let { id ->
                    val args = Bundle()
                    args.putInt(Constants.KEY_MOVIE_ID_BUNDLE, id)
                    findNavController().navigate(
                        R.id.action_movieListFragment_to_movieDetailFragment, args
                    )
                }
            }
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        if (this.itemCount == 0) {
                            binding.msvMovie.viewState = MultiStateView.ViewState.LOADING
                        } else {
                            binding.msvMovie.viewState = MultiStateView.ViewState.LOADING
                        }
                    }

                    is LoadState.NotLoading -> {
                        binding.msvMovie.viewState = MultiStateView.ViewState.CONTENT
//                        binding.tvMovieListTitle.text = genreName
                    }

                    is LoadState.Error -> {
                        showErrorDialog(
                            onPositiveBtnClick = { fetchData() }
                        )
                    }
                }
            }
        }

        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
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
}
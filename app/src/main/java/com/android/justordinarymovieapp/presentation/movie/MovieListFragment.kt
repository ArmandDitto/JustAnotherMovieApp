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
import com.android.justordinarymovieapp.utils.setAutoNullAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : BaseFragment<FragmentMovieListBinding>() {

    private val viewModel: MovieViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter

    private val genreId by lazy { arguments?.getInt(KEY_GENRE_ID, 0) }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieListBinding
        get() = FragmentMovieListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupView()
        setUpObserver()
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
            genreId?.let { viewModel.fetchMoviesByGenre(it) }
        }
    }

    private fun setupView() {
        movieAdapter = MovieAdapter(requireContext()).apply {
            onRootClick = {
                it.id?.let { id ->
                    val args = Bundle()
                    args.putInt(KEY_MOVIE_ID, id)
                    findNavController().navigate(
                        R.id.action_movieListFragment_to_movieDetailFragment, args
                    )
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

    companion object {

        const val KEY_GENRE_ID = "KEY_GENRE_ID"
        const val KEY_MOVIE_ID = "KEY_MOVIE_ID"

        fun newInstance(genreId: Int): MovieListFragment {
            return MovieListFragment().apply {
                val bundle = Bundle()
                bundle.putInt(KEY_GENRE_ID, genreId)
                arguments = bundle
            }
        }
    }
}
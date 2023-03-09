package com.android.justordinarymovieapp.presentation.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.FragmentGenreListBinding
import com.android.justordinarymovieapp.presentation.movie.MovieListFragmentDirections
import com.android.justordinarymovieapp.utils.Constants
import com.kennyc.view.MultiStateView
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreListFragment : BaseFragment<FragmentGenreListBinding>() {

    private val viewModel: GenreViewModel by viewModel()
    private lateinit var genreAdapter: GenreAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenreListBinding
        get() = FragmentGenreListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreAdapter = GenreAdapter().apply {
            onClick = {
                val args = Bundle()
                args.putInt(Constants.KEY_GENRE_ID_BUNDLE, it.id ?: 0)
                args.putString(Constants.KEY_GENRE_NAME_BUNDLE, it.name)
                findNavController().navigate(
                    R.id.action_genreListFragment_to_movieListFragment, args
                )
            }
        }

        binding.rvGenre.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = genreAdapter
        }
        initObserver()
        setupListener()
        viewModel.fetchGenres()
    }

    private fun setupListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.fetchGenres()
        }
    }

    private fun initObserver() {
        viewModel.genresLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.msvGenre.viewState = MultiStateView.ViewState.LOADING
                }

                is ResultWrapper.Success -> {
                    binding.msvGenre.viewState = MultiStateView.ViewState.CONTENT
                    it.value.genres?.let { genres -> genreAdapter.addItems(genres) }
                }

                is ResultWrapper.Error -> {
                    showErrorDialog(
                        desc = it.message,
                        onPositiveBtnClick = { viewModel.fetchGenres() },
                        isCancelable = true
                    )
                }
            }
        }
    }
}
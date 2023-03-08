package com.android.justordinarymovieapp.presentation.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.FragmentGenreListBinding
import com.android.justordinarymovieapp.presentation.movie.MovieListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreListFragment : BaseFragment<FragmentGenreListBinding>() {

    private val viewModel: GenreViewModel by viewModel()
    private lateinit var genreAdapter: GenreAdapter

    private var genreId: Int = 0

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenreListBinding
        get() = FragmentGenreListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreAdapter = GenreAdapter().apply {
            onClick = {
                it.id?.let { id ->
                    genreId = id
                    MovieListActivity.launchIntent(requireContext())
                }
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
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultWrapper.Success -> {
                    binding.progressBar.visibility = View.GONE
                    it.value.genres?.let { genres -> genreAdapter.addItems(genres) }
                    genreId = it.value.genres?.get(0)?.id ?: 0
                }

                is ResultWrapper.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorDialog(
                        desc = it.message,
                        onPositiveBtnClick = { viewModel.fetchGenres() }
                    )
                }
            }
        }
    }

    companion object {

        fun newInstance(): GenreListFragment {
            return GenreListFragment().apply {
                val bundle = Bundle()
                arguments = bundle
            }
        }
    }
}
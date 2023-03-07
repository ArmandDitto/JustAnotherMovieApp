package com.android.justordinarymovieapp.presentation.movie

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseFragment
import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.android.justordinarymovieapp.databinding.FragmentMovieBinding
import com.android.justordinarymovieapp.presentation.genre.GenreAdapter
import com.android.justordinarymovieapp.presentation.genre.GenreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : BaseFragment<FragmentMovieBinding>() {

    private val viewModel: GenreViewModel by viewModel()
    private lateinit var genreAdapter: GenreAdapter

    private var genreId: Int = 0

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMovieBinding
        get() = FragmentMovieBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreAdapter = GenreAdapter().apply {
            onClick = {
                it.id?.let { id ->
                    genreId = id
                    initFragment()
                }
            }
        }

        binding.rvGenre.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = genreAdapter
        }
        initObserver()
        viewModel.fetchGenres()
    }

    private fun initObserver() {
        viewModel.genreLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }
                is ResultWrapper.Success -> {
                    it.value.genres?.let { genres -> genreAdapter.addItems(genres) }
                    genreId = it.value.genres?.get(0)?.id ?: 0
                }
                is ResultWrapper.Error -> {
                    AlertDialog.Builder(activity)
                        .setTitle("Error")
                        .setMessage("Error")
                        .setPositiveButton("Retry") { dialog, _ ->
                            dialog?.dismiss()
                            viewModel.fetchGenres()
                        }
                        .show()
                }
            }
        }
    }

    private fun initFragment() {
        childFragmentManager.beginTransaction()
            .replace(binding.flContainer.id, MovieListFragment.newInstance(genreId))
            .commit()
    }

    companion object {

        fun newInstance(): MovieFragment {
            return MovieFragment().apply {
                val bundle = Bundle()
                arguments = bundle
            }
        }
    }
}
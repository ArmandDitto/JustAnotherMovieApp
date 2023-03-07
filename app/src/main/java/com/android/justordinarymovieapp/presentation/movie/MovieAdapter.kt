package com.android.justordinarymovieapp.presentation.movie

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.justordinarymovieapp.base.paging.PagingFooterTextViewHolder
import com.android.justordinarymovieapp.databinding.ItemMovieBinding
import com.android.justordinarymovieapp.databinding.LayoutPagingLoadStateBinding
import com.android.justordinarymovieapp.model.MovieResponse
import com.android.justordinarymovieapp.base.paging.PagingUiModel
import com.android.justordinarymovieapp.utils.viewBinding

class MovieAdapter() :
    PagingDataAdapter<PagingUiModel<MovieResponse>, RecyclerView.ViewHolder>(
        DiffUtilCallback()
    ) {

    var onRootClick: ((data: MovieResponse) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: MovieResponse) {
            with(binding) {
                tvMovieIndex.text = layoutPosition.toString()
                tvMovieName.text = data.title

                root.setOnClickListener { onRootClick?.invoke(data) }
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PagingUiModel<MovieResponse>>() {
        override fun areItemsTheSame(
            oldItem: PagingUiModel<MovieResponse>,
            newItem: PagingUiModel<MovieResponse>
        ): Boolean {
            return (oldItem is PagingUiModel.DataItem && newItem is PagingUiModel.DataItem &&
                    oldItem.data.id == newItem.data.id) ||
                    (oldItem is PagingUiModel.SeparatorItem && newItem is PagingUiModel.SeparatorItem &&
                            oldItem.description == newItem.description)
        }

        override fun areContentsTheSame(
            oldItem: PagingUiModel<MovieResponse>,
            newItem: PagingUiModel<MovieResponse>
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is PagingUiModel.DataItem -> (holder as ViewHolder).bindView(uiModel.data)
                is PagingUiModel.SeparatorItem -> (holder as PagingFooterTextViewHolder).bindView(
                    uiModel.description
                )
                null -> throw UnsupportedOperationException("Unknown view")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ViewHolder(parent.viewBinding(ItemMovieBinding::inflate))
            else -> PagingFooterTextViewHolder(parent.viewBinding(LayoutPagingLoadStateBinding::inflate))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingUiModel.DataItem -> 1
            is PagingUiModel.SeparatorItem -> 2
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

}
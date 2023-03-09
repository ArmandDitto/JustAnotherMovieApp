package com.android.justordinarymovieapp.presentation.review

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.justordinarymovieapp.base.paging.PagingFooterTextViewHolder
import com.android.justordinarymovieapp.base.paging.PagingUiModel
import com.android.justordinarymovieapp.databinding.ItemReviewBinding
import com.android.justordinarymovieapp.databinding.LayoutPagingLoadStateBinding
import com.android.justordinarymovieapp.model.review.Review
import com.android.justordinarymovieapp.utils.viewBinding
import com.bumptech.glide.Glide

class ReviewPagingAdapter (val context: Context) :
    PagingDataAdapter<PagingUiModel<Review>, RecyclerView.ViewHolder>(
        DiffUtilCallback()
    ) {

    var onRootClick: ((data: Review) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: Review) {
            with(binding) {
                tvReviewerName.text = data.author
                tvComment.text = data.content
                rbScore.setReviewScore(data.authorDetails?.rating ?: 0F)
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/original/${data.authorDetails?.avatarPath}")
                    .into(binding.ivReviewer)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<PagingUiModel<Review>>() {
        override fun areItemsTheSame(
            oldItem: PagingUiModel<Review>,
            newItem: PagingUiModel<Review>
        ): Boolean {
            return (oldItem is PagingUiModel.DataItem && newItem is PagingUiModel.DataItem &&
                    oldItem.data.id == newItem.data.id) ||
                    (oldItem is PagingUiModel.SeparatorItem && newItem is PagingUiModel.SeparatorItem &&
                            oldItem.description == newItem.description)
        }

        override fun areContentsTheSame(
            oldItem: PagingUiModel<Review>,
            newItem: PagingUiModel<Review>
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
            1 -> ViewHolder(parent.viewBinding(ItemReviewBinding::inflate))
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
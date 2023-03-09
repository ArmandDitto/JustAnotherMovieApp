package com.android.justordinarymovieapp.presentation.review

import android.annotation.SuppressLint
import com.android.justordinarymovieapp.base.BaseBindingAdapter
import com.android.justordinarymovieapp.base.BaseBindingViewHolder
import com.android.justordinarymovieapp.databinding.ItemReviewBinding
import com.android.justordinarymovieapp.model.review.Review
import com.bumptech.glide.Glide

class ReviewAdapter : BaseBindingAdapter<BaseBindingViewHolder, ItemReviewBinding>() {

    var listItems = mutableListOf<Review>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<Review>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateBinding(
        holder: BaseBindingViewHolder,
        binding: ItemReviewBinding,
        position: Int
    ) {
        val data = listItems[position]

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
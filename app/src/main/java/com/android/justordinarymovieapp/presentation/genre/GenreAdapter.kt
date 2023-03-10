package com.android.justordinarymovieapp.presentation.genre

import android.annotation.SuppressLint
import com.android.justordinarymovieapp.base.BaseBindingAdapter
import com.android.justordinarymovieapp.base.BaseBindingViewHolder
import com.android.justordinarymovieapp.databinding.ItemGenreBinding
import com.android.justordinarymovieapp.model.genre.Genre
import com.android.justordinarymovieapp.model.genre.GenreResponse
import com.android.justordinarymovieapp.utils.Utils


class GenreAdapter : BaseBindingAdapter<BaseBindingViewHolder, ItemGenreBinding>() {

    var listItems = mutableListOf<Genre>()
    var onClick: ((genre: Genre) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<Genre>) {
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
        binding: ItemGenreBinding,
        position: Int
    ) {
        val data = listItems[position]

        with(binding) {
            val color = data.id?.let { Utils().getRandomColor(it) }

            tvGenreName.text = data.name
            tvIcon.text = data.name?.get(0).toString()

            if (color != null) {
                tvIcon.setBackgroundColor(color)
            }
            cvGenre.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }
}
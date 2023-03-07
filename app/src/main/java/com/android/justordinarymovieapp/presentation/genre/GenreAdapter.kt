package com.android.justordinarymovieapp.presentation.genre

import android.annotation.SuppressLint
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseBindingAdapter
import com.android.justordinarymovieapp.base.BaseBindingViewHolder
import com.android.justordinarymovieapp.databinding.ItemGenreBinding
import com.android.justordinarymovieapp.model.genre.GenreResponse

class GenreAdapter : BaseBindingAdapter<BaseBindingViewHolder, ItemGenreBinding>() {

    var listItems = mutableListOf<GenreResponse.Genre>()
    var onClick: ((genre: GenreResponse.Genre) -> Unit)? = null

    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<GenreResponse.Genre>) {
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
            tvGenreName.text = data.name

            if(position == selectedItemPos) {
                tvGenreName.setTextColor(context.getColor(R.color.soft_green))
                cvGenre.strokeColor = context.getColor(R.color.soft_green)
            } else {
                tvGenreName.setTextColor(context.getColor(R.color.dark))
                cvGenre.strokeColor = context.getColor(R.color.dark)
            }

            cvGenre.setOnClickListener {
                onClick?.invoke(data)
                selectedItemPos = position
                lastItemSelectedPos = if(lastItemSelectedPos == -1)
                    selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
            }
        }
    }
}
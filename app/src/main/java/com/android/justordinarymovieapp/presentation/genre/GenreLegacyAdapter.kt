package com.android.justordinarymovieapp.presentation.genre

import android.annotation.SuppressLint
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.base.BaseBindingAdapter
import com.android.justordinarymovieapp.base.BaseBindingViewHolder
import com.android.justordinarymovieapp.databinding.ItemGenreLegacyBinding
import com.android.justordinarymovieapp.model.genre.GenreResponse

class GenreLegacyAdapter : BaseBindingAdapter<BaseBindingViewHolder, ItemGenreLegacyBinding>() {

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
        binding: ItemGenreLegacyBinding,
        position: Int
    ) {
        val data = listItems[position]

        with(binding) {
            tvGenreName.text = data.name

            if (position == selectedItemPos) {
                tvGenreName.setTextColor(context.getColor(R.color.color_primary))
                cvGenre.strokeColor = context.getColor(R.color.color_primary)
            } else {
                tvGenreName.setTextColor(context.getColor(R.color.text_android_black_default))
                cvGenre.strokeColor = context.getColor(R.color.text_android_black_default)
            }

            cvGenre.setOnClickListener {
                onClick?.invoke(data)
                selectedItemPos = position
                lastItemSelectedPos = if (lastItemSelectedPos == -1)
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
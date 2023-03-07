package com.android.justordinarymovieapp.base.paging

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.justordinarymovieapp.databinding.LayoutPagingLoadStateBinding
import com.android.justordinarymovieapp.utils.viewBinding

class PagingLoadStateAdapter : LoadStateAdapter<PagingLoadStateAdapter.ViewHolder>() {

    var onRetry: (() -> Unit)? = null

    inner class ViewHolder(private val binding: LayoutPagingLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(loadState: LoadState) {
            with(binding) {
                progressBar.isVisible = loadState is LoadState.Loading
                when (loadState) {
                    is LoadState.Error -> {
                        tvMessage.apply {
                            text = loadState.error.message
                            isVisible = true
                        }
                        btnRetry.isVisible = true
                    }
                    else -> {
                        tvMessage.isVisible = false
                        btnRetry.isVisible = false
                    }
                }
                btnRetry.setOnClickListener { onRetry?.invoke() }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bindView(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(parent.viewBinding(LayoutPagingLoadStateBinding::inflate))
    }

}
package com.android.justordinarymovieapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> ViewGroup.viewBinding(bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T) =
    bindingInflater(LayoutInflater.from(context), this, false)

fun <VH : RecyclerView.ViewHolder> RecyclerView.setAutoNullAdapter(
    adapter: RecyclerView.Adapter<VH>
) {
    this.adapter = adapter
    this.clearAdapter()
}

internal fun RecyclerView.clearAdapter() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View) {

        }

        override fun onViewDetachedFromWindow(p0: View) {
            this@clearAdapter.adapter = null
        }

    })
}
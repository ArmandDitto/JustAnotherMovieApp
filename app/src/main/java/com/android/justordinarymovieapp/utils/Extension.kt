package com.android.justordinarymovieapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDirections
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

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun Context.showDefaultToast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, message, duration).show()
}
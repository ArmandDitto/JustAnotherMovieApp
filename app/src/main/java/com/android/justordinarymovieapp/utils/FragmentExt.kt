package com.android.justordinarymovieapp.utils

import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.android.justordinarymovieapp.R

fun Activity.setupToolbar(binding: ViewBinding, title: String) {
    val tvTitle = binding.root.findViewById<TextView>(R.id.tv_title)
    val ivBack = binding.root.findViewById<ImageView>(R.id.iv_back)
    tvTitle.text = title
    ivBack.setOnClickListener { onBackPressed() }
}

fun Fragment.setupToolbar(binding: ViewBinding, title: String) {
    activity?.setupToolbar(binding, title)
}
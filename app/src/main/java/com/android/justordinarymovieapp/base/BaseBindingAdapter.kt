package com.android.justordinarymovieapp.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseBindingAdapter<T : BaseBindingViewHolder, VB : ViewBinding> :
    RecyclerView.Adapter<T>() {

    protected lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[1] as Class<VB>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val bindData = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        context = parent.context
        return BaseBindingViewHolder(bindData) as T
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        context = holder.binding.root.context
        updateBinding(holder, holder.binding as VB, position)
    }

    protected abstract fun updateBinding(
        holder: T,
        binding: VB, position: Int
    )

}
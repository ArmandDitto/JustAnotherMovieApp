package com.android.justordinarymovieapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding)
    protected abstract val bindingInflater: (LayoutInflater) -> VB

    @CallSuper
    override fun onStart() {
        super.onStart()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
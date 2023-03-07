package com.android.justordinarymovieapp.presentation.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.databinding.ActivityMovieBinding

class MovieActivity : BaseActivity<ActivityMovieBinding>() {

    private val fragment by lazy { MovieFragment.newInstance() }

    override val bindingInflater: (LayoutInflater) -> ActivityMovieBinding
        get() = ActivityMovieBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.flContainer.id, fragment)
            .commit()
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, MovieActivity::class.java)
            context.startActivity(intent)
        }
    }
}
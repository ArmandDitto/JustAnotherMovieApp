package com.android.justordinarymovieapp.presentation.genre

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.databinding.ActivityGenreListBinding

class GenreListActivity : BaseActivity<ActivityGenreListBinding>() {

    private val fragment by lazy { GenreListFragment.newInstance() }

    override val bindingInflater: (LayoutInflater) -> ActivityGenreListBinding
        get() = ActivityGenreListBinding::inflate

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
            val intent = Intent(context, GenreListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
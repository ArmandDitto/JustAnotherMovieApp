package com.android.justordinarymovieapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.databinding.ActivityMovieContainerBinding

class ContainerMovieActivity : BaseActivity<ActivityMovieContainerBinding>() {

    private lateinit var navController: NavController

    override val bindingInflater: (LayoutInflater) -> ActivityMovieContainerBinding
        get() = ActivityMovieContainerBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding. mainFragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, ContainerMovieActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
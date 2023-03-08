package com.android.justordinarymovieapp.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.android.justordinarymovieapp.base.BaseActivity
import com.android.justordinarymovieapp.databinding.ActivitySplashBinding
import com.android.justordinarymovieapp.presentation.ContainerMovieActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            ContainerMovieActivity.launchIntent(this)
            finish()
        }, 3000)
    }

}
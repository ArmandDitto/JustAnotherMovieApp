package com.android.justordinarymovieapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.android.justordinarymovieapp.di.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JustAnotherMovieApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        instance = this
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@JustAnotherMovieApp)
            modules(Module.getAll())
        }
    }

    companion object {
        lateinit var instance: JustAnotherMovieApp
            private set

        fun getApp(): JustAnotherMovieApp {
            return instance
        }

        fun getContext(): Context {
            return instance
        }
    }
}
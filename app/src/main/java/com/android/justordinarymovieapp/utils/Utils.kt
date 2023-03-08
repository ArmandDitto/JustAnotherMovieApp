package com.android.justordinarymovieapp.utils

import android.graphics.Color

class Utils {

    fun getRandomColor(randomInt: Int) : Int {
        return Color.argb(255, 200, 0, randomInt)
    }

}
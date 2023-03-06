package com.android.justordinarymovieapp.utils

object JNIUtil {

    init {
        System.loadLibrary("constant")
    }

    val baseUrl: String get() { return getBaseUrlImpl() }

    //From Flavors Gradle
    private external fun getBaseUrlImpl(): String

}
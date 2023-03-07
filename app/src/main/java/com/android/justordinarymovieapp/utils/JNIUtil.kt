package com.android.justordinarymovieapp.utils

object JNIUtil {

    init {
        System.loadLibrary("constant")
    }

    val baseUrl: String get() { return getBaseUrlImpl() }
    val apiKey: String get() { return getTMDBApiKey() }

    //From Flavors Gradle
    private external fun getBaseUrlImpl(): String
    private external fun getTMDBApiKey(): String

}
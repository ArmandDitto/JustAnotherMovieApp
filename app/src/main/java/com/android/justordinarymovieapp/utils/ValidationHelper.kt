package com.android.justordinarymovieapp.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ValidationHelper {

    companion object {
        fun isValidJson(message: String): Boolean {
            try {
                JSONObject(message)
            } catch (ex: JSONException) {
                // Check if JSONArray is valid as well…
                try {
                    JSONArray(message)
                } catch (ex1: JSONException) {
                    return false
                }

            }

            return true
        }
    }

}
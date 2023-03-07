package com.android.justordinarymovieapp.base.network

import com.android.justordinarymovieapp.utils.ValidationHelper
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject

open class ErrorResponse() {

    @SerializedName("status_message")
    var message: String? = null

    @SerializedName("status_code")
    var code: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    constructor(response: String) : this() {
        var isValid = true
        if (ValidationHelper.isValidJson(response)) {
            try {
                val json = JSONObject(response)
                message = if (json.has("error")) {
                    json.optString("error")
                } else {
                    json.optString("message")
                }
            } catch (e: JSONException) {
                isValid = false
            }
        } else {
            isValid = false
        }

        if (!isValid) message = ""
    }

}
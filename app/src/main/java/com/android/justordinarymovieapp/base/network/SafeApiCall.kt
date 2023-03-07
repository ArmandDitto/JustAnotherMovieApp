package com.android.justordinarymovieapp.base.network

import com.android.justordinarymovieapp.base.model.ResultWrapper
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.Error(
                    null,
                    throwable.localizedMessage ?: "Connection Problem"
                )
                is HttpException -> ResultWrapper.Error(
                    throwable.code(),
                    parseRetrofitException(throwable)
                )
                is JsonDataException -> ResultWrapper.Error(null, "Parsing data error")
                else -> ResultWrapper.Error(null, throwable.message)
            }
        }
    }
}

fun parseRetrofitException(exception: HttpException): String {
    var errorMessage = ""
    val httpStatusCode = HttpStatusCode.values().firstOrNull { it.code == exception.code() }
        ?: HttpStatusCode.Unknown
    val errorCode = exception.code()
    val stream = exception.response()?.errorBody()?.byteStream()
    val errorJson = stream?.bufferedReader()?.use(BufferedReader::readText)

    errorJson?.let {
        errorMessage = ErrorResponse(it).message ?: ""
    }

    errorMessage =
        if (errorMessage.isBlank() || errorMessage.equals(null) || errorMessage == "null") {
            "$errorCode - ${httpStatusCode.statusName}"
        } else {
            "$errorCode - $errorMessage"
        }

    return errorMessage
}
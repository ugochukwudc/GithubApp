package com.example.myapplication.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

inline fun <reified T : Any> intrinsicTry(
    mCallExceptions: MutableLiveData<Throwable>,
    block: () -> T?
): T? {
    return try {
        block.invoke()
    } catch (e: Exception) {
        Log.e("ApiError", "Api call failed for ${T::class.java.simpleName}", e)
        mCallExceptions.postValue(e)
        null
    }
}
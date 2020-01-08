package com.example.myapplication.service

import android.content.Context
import com.example.myapplication.models.Login
import kotlinx.coroutines.delay
import retrofit2.Response

class R1Service(context: Context) {
    private val mApiClient = R1ApiClient.create(context)

    /**
     * Attempts to fetch User details from [R1ApiClient] for [userId]
     * @return [Login] on successful login
     */
    suspend fun loginUser(userId: String): Response<Login> {
        delay(10_000) // 10 sec delay
        return mApiClient.login(userId)
    }

}

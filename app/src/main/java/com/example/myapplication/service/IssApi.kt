package com.example.myapplication.service;

import com.example.myapplication.models.IssData
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IssApi {
    companion object Factory {
        private const val BASE_URL = "http://api.open-notify.org"
        fun create(): IssApi {
            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(IssApi::class.java)
        }

    }

    @GET("iss-now.json")
    fun fetchPosition(): Call<IssData>

    @GET("iss-now.json")
    suspend fun fetchPos(): Response<IssData>
}

package com.example.myapplication.service

import com.example.myapplication.models.IssData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Response

class IssService {
    private val mClient = IssApi.create()

    fun fetchPosition(): Call<IssData> {
        return mClient.fetchPosition()
    }

    suspend fun getPosition(): Response<IssData> {
        return mClient.fetchPos()
    }

    @ExperimentalCoroutinesApi
    fun fetchPositionInfinite(delayMs: Long): Flow<Response<IssData>> {
        return flow {
            while (true) {
                emit(getPosition())
                delay(delayMs)
            }
        }.flowOn(Dispatchers.IO)
    }
}
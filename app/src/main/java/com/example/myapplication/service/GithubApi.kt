package com.example.myapplication.service

import android.content.Context
import android.util.Log
import com.example.myapplication.models.GitHubApiError
import com.example.myapplication.models.RepoGson
import com.example.myapplication.models.UserGson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    companion object Factory {
        private const val BASE_URL = "https://api.github.com"
        private const val PATH_USER = "user"

        fun create(context: Context): GithubApi {
            return Retrofit.Builder()
                .client(
                    okHttpClient
                        .newBuilder()
                        .addInterceptor(ConnectionInterceptor(ConnectivityManagerWrapper(context)))
                        .addInterceptor {chain ->
                            val response = chain.proceed(chain.request())

                            Log.d(
                                "THIS",
                                """Response isSuccessful ${response.isSuccessful}
                                Response is redirect ${response.isRedirect}
                                Response prior is ${response.priorResponse()}
                                Response cached is ${response.cacheResponse()}
                                Response code is ${response.code()}
                                 Response body is ${response.body()}
                                    """
                            )
                            return@addInterceptor when{
                                response.isSuccessful -> response
                                else -> {
                                    val throwable: Throwable = try {
                                        // parse JSON Error
                                        val error: GitHubApiError = Gson().fromJson(
                                            response.body()?.string(),
                                            GitHubApiError::class.java
                                        )
                                        Log.d("THIS", "error is $error")
                                        error
                                    } catch (e: Throwable) {
                                        Log.e("THIS", "Exception converting error $e")
                                        e
                                    }

                                    if (throwable is GitHubApiError){
                                        throw throwable
                                    } else {
                                        response
                                    }
                                }
                            }
                        }
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl(BASE_URL)
                .build()
                .create(GithubApi::class.java)
        }
    }

    @GET("users")
    suspend fun fetchUsers(): Response<List<UserGson>>

    @GET("users/{$PATH_USER}/repos")
    suspend fun fetchRepos(@Path(PATH_USER) user: String): Response<List<RepoGson>>

    @GET("users/{$PATH_USER}")
    suspend fun fetchUser(@Path(PATH_USER) user: String): Response<UserGson>



}
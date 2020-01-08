package com.example.myapplication.service

import android.content.Context
import android.util.Log
import com.example.myapplication.models.ApiError
import com.example.myapplication.models.Login
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface R1ApiClient {
    companion object Factory{
        private const val BASE_URL = "https://www.r1management.ca/mobile/"
        private const val LOGIN_PATH = "padinXml.php"
        private const val LOGIN_QUERY = "code"

        fun create(context: Context): R1ApiClient {
            val serializer = Persister()
            return Retrofit.Builder()
                .client(
                    okHttpClient
                        .newBuilder()
                        .addInterceptor(ConnectionInterceptor(ConnectivityManagerWrapper(context)))
/*
                        .addInterceptor { chain ->
                            val response = chain.proceed(chain.request())
                            val responseString = response.body()?.string()
                            Log.d("THIS", """$responseString
                                | Response is successful = ${response.isSuccessful}
                            """.trimMargin())

                            // try to parse the error xml and throw the exception
                            val error: Throwable = try {
                                serializer.read(ApiError::class.java, responseString, false)
                            } catch (e: Throwable) {
                                Log.e("THIS", "error parsing error string", e)
                                e
                            }

                            if (error is ApiError)
                                throw error
                            else
                                response
                        }
*/
                        .build()
                )
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
                .baseUrl(BASE_URL)
                .build()
                .create(R1ApiClient::class.java)
        }
    }

    @GET(LOGIN_PATH)
    suspend fun login(@Query(LOGIN_QUERY) loginId: String): Response<Login>

}
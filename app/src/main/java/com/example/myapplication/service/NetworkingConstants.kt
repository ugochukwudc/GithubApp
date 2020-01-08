package com.example.myapplication.service

import android.content.Context
import android.net.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import java.util.logging.Logger

object okHttpClient: OkHttpClient()

class ConnectionInterceptor(private val cm: ConnectivityManagerWrapper): Interceptor{
    private val logger = Logger.getLogger(ConnectionInterceptor::class.java.name)

    override fun intercept(chain: Interceptor.Chain): Response {
        logger.log(
            Level.FINE,
            "Connection Interceptor, isConnected = ${cm.isConnected()}, isAvailable = ${cm.isInternetAvailable()} $chain"
        )
        return when {
            !cm.isConnected() -> throw NoNetworkException()
            !cm.isInternetAvailable() -> throw NoInternetException()
            else -> chain.proceed(chain.request())
        }
    }
}


class ConnectivityManagerWrapper(context: Context){
    private val connectivityManager: ConnectivityManager
    private val connected: AtomicBoolean
    private val networkCallBack: ConnectivityManager.NetworkCallback

    init {
        connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connected = AtomicBoolean(false)
        networkCallBack = object : ConnectivityManager.NetworkCallback(){
            override fun onLost(network: Network) {
                super.onLost(network)
                connected.set(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                connected.set(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connected.set(true)
            }
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallBack)
    }

    fun isConnected(): Boolean {
        return connected.get()
    }

    /**
     * Attempt to open a connection to Google 8.8.8.8 DNS server
     */
    fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val inetSocketAddress = InetSocketAddress("8.8.8.8", 53)

            sock.connect(inetSocketAddress, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }
}

class NoNetworkException: IOException("No Network Detected, please connect to a network")
class NoInternetException: IOException("No Internet detected on this Network, Please connect to a network with internet")


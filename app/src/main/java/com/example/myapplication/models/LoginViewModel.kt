package com.example.myapplication.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewModelScope
import com.example.myapplication.enums.LoadingState
import com.example.myapplication.service.R1Service
import com.example.myapplication.util.intrinsicTry
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.concurrent.CancellationException


/**
 * ViewModel for the LoginActivity
 * @property service [R1Service] for executing R1Management Api calls
 */
class LoginViewModel(private val service: R1Service) : LoadingViewModel() {
    // Used to Keep track of the code entered by User
    val userCodeInput: MutableLiveData<String> = MutableLiveData()
    private val mValidationResult: MediatorLiveData<LoginResult> = MediatorLiveData()
    val validationResult: LiveData<String> = map(mValidationResult, LoginResult::message)
    private val mLoginFailed: MutableLiveData<String> = MutableLiveData()
    private val mApiErrorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    val errorLiveData: LiveData<Throwable> = mApiErrorLiveData
    private val mUserData: MutableLiveData<Login> = MutableLiveData()
    val canLogin = map(userCodeInput){input -> input.all { it.isDigit() } && input.length == 6 }
    val loggedIn: LiveData<Boolean> = map(mUserData) { it != null }
    val userData: LiveData<Login> = mUserData

    init {

        mValidationResult.addSource(userCodeInput){input ->
            if (input.all { it.isDigit() } && input.length == 6){
                LoginResult.Success
            } else {
                LoginResult.InputError
            }
        }

        mValidationResult.addSource(mLoginFailed){
            LoginResult.LoginError(it)
        }
    }
    /**
     * Invoked when user clicks the login button to send APi call to verify user
     */
    fun onLogin() {
        viewModelScope.launch {
            setLoadingState(LoadingState.STARTED)

            val loginResponse: Response<Login>? =
                intrinsicTry(mApiErrorLiveData) {
                    service.loginUser(userCodeInput.value!!)
                }

            Log.d("THIS", "response is $loginResponse")

            // delay another 10 sec
            delay(10_000)
            val loginData = loginResponse?.body()

            Log.d("THIS", "response is $loginData")

            when(loginData?.isValidUser()) {
                true -> {
                    setLoadingState(LoadingState.COMPLETE)
                    mUserData.postValue(loginData)
                }
                false -> {
                    // is not valid user, post error
                    setLoadingState(LoadingState.ERROR)
                    mLoginFailed.postValue(loginData.error)
                }
                else -> {
                    mLoginFailed.postValue(loginResponse?.raw().toString())
                    setLoadingState(LoadingState.ERROR)
                }
            }
        }
    }

    fun onLogout() {
        mUserData.postValue(null)
    }

    fun cancelLogin() {
        viewModelScope.coroutineContext.cancelChildren(CancellationException("Back Button Pressed"))
        setLoadingState(LoadingState.NOT_STARTED)
    }
}

sealed class LoginResult(open val message: String = "") {
    object Success : LoginResult()
    object InputError : LoginResult("Invalid Input, Please enter a correct Login Code")
    class LoginError(override val message: String) : LoginResult(message)
}
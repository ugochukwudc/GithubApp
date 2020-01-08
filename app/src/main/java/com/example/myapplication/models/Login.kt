package com.example.myapplication.models

/**
 * Representative data class for successful response from the login api
 */
data class Login(
    var role: String? = null,
    var name: String? = null,
    var tod: String? = null,
    var employer: String? = null,
    var error: String? = null,
    var event: String? = null,
    var job: String? = null,
    var jobname: String? = null
) {
    fun isValidUser(): Boolean {
        return error.isNullOrEmpty() && !name.isNullOrEmpty()
    }

    fun getApiError(): ApiError {
        return ApiError(error)
    }
}

data class LoginResponse(var login: Login?= null)
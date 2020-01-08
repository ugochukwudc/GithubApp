package com.example.myapplication.models


import com.google.gson.annotations.SerializedName
import java.io.IOException

/**
 *
 * {
 *  "error": "Not Found",
 *  "documentation_url": "https://developer.github.com/v3/users/#get-a-single-user"
 * }
 *
 */
data class GitHubApiError(
    @SerializedName("error")
    override val message: String? = null,
    @SerializedName("documentation_url")
    var documentationUrl: String? = null
): IOException()
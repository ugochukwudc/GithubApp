package com.example.myapplication.models


import com.google.gson.annotations.SerializedName

data class IssPosition(
    @SerializedName("longitude")
    var longitude: String? = null,
    @SerializedName("latitude")
    var latitude: String? = null
)
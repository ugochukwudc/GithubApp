package com.example.myapplication.models


import com.google.gson.annotations.SerializedName

/**
 * {"iss_position": {"longitude": "150.7474", "latitude": "47.3686"}, "timestamp": 1571185556, "error": "success"}
 */
data class IssData(
    @SerializedName("error")
    var message: String? = null,
    @SerializedName("timestamp")
    var timestamp: Int? = null,
    @SerializedName("iss_position")
    var issPosition: IssPosition? = null
)
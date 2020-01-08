package com.example.myapplication.models

import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Root
import java.io.IOException

/**
 * ApiError Result data class corresponds to <error></error> xml tag
 */
@Root(name = "login", strict = false)
data class ApiError(@SerializedName(ERROR_TAG) override var message: String?=null): IOException(){
    companion object {
        const val ERROR_TAG = "error"
    }

}
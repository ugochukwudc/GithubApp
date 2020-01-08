package com.example.myapplication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.models.LoginViewModel
import com.example.myapplication.service.R1Service
import com.example.myapplication.util.SingletonHolder

class AppViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    companion object : SingletonHolder<AppViewModelFactory, Application>(::AppViewModelFactory)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val context = this.application.applicationContext
        return when {
            modelClass.isAssignableFrom(TestViewModel::class.java) -> TestViewModel(context) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(R1Service(context)) as T
            else -> super.create(modelClass)
        }
    }
}
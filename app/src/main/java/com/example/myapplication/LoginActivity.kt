package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Browser
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.enums.LoadingState
import com.example.myapplication.models.GitHubApiError
import com.example.myapplication.models.LoginViewModel
import com.example.myapplication.service.NoInternetException
import com.example.myapplication.service.NoNetworkException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val viewModel = ViewModelProviders.of(
            this, AppViewModelFactory.getInstance(application)
        ).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        with(binding) {
            viewModel.errorLiveData.observe(this@LoginActivity, Observer { error ->
                when(error){
                    is NoNetworkException, is NoInternetException -> Snackbar
                        .make(root, error.message!!, Snackbar.LENGTH_SHORT)
                        .setAction("SETTINGS"){
                            startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                        }.show()
                    else -> Toast.makeText(applicationContext, "Error ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
            viewModel.loggedIn.observe(this@LoginActivity, Observer {
                val message = if (it) {
                    // logged in
                    """User ${viewModel.userData.value} has Logged in successfully"""
                } else {
                    // logged out
                    """User Logged out complete"""
                }
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
            })

            viewModel.validationResult
        }
    }

    override fun onBackPressed() {
        when (binding.viewModel?.loadingState?.value) {
            LoadingState.COMPLETE -> (binding.viewModel as LoginViewModel).onLogout()
            LoadingState.STARTED -> (binding.viewModel as LoginViewModel).cancelLogin()
            else -> super.onBackPressed()
        }
    }

}

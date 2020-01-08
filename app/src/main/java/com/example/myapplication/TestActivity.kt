package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Browser
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.databinding.ActivityTestBinding
import com.example.myapplication.enums.LoadingState
import com.example.myapplication.models.GitHubApiError
import com.example.myapplication.service.NoInternetException
import com.example.myapplication.service.NoNetworkException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        val viewModel = ViewModelProviders.of(
            this, AppViewModelFactory.getInstance(application)
        ).get(TestViewModel::class.java)
        val arrayAdapter = ArrayAdapter<String>(this@TestActivity, android.R.layout.simple_list_item_1)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        with(binding) {
            displayList.adapter = arrayAdapter
            viewModel.listItems.observe(this@TestActivity, Observer {
                arrayAdapter.clear()
                arrayAdapter.addAll(it)
            })
            viewModel.loadingState.observe(this@TestActivity, Observer{
                when(it) {
                    LoadingState.COMPLETE -> {
                        progressBar.visibility = View.GONE
                        mainGroup.visibility = View.GONE
                        displayList.visibility = View.VISIBLE
                    }
                    LoadingState.ERROR -> {
                        progressBar.visibility = View.GONE
                        mainGroup.visibility = View.VISIBLE
                        displayList.visibility = View.GONE
                        Toast.makeText(applicationContext, "Fetch Failed See Logs", Toast.LENGTH_LONG).show()
                    }
                    LoadingState.STARTED -> {
                        progressBar.visibility = View.VISIBLE
                        mainGroup.visibility = View.GONE
                        displayList.visibility = View.GONE
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                        mainGroup.visibility = View.VISIBLE
                        displayList.visibility = View.GONE
                    }
                }
            })

            viewModel.errorLiveData.observe(this@TestActivity, Observer { error ->
                when(error){
                    is NoNetworkException, is NoInternetException -> Snackbar
                        .make(root, error.message!!, Snackbar.LENGTH_SHORT)
                        .setAction("SETTINGS"){
                            startActivity(Intent(Settings.ACTION_SETTINGS))
                        }.show()
                    is GitHubApiError -> {
                        val browserIntent: Intent = Intent.parseUri(error.documentationUrl, 0)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .putExtra(Browser.EXTRA_APPLICATION_ID, packageName)

                        Snackbar.make(root, error.message!!, Snackbar.LENGTH_SHORT)
                            .setAction("See Why") { startActivity(browserIntent) }
                            .show()
                    }
                    else -> Toast.makeText(applicationContext, "Error ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

            buttonFetchRepos.setOnClickListener {
                viewModel.onReposRequested()
            }

            buttonFetchRepos.setOnLongClickListener {
                viewModel.onMore()
                return@setOnLongClickListener true
            }

            buttonFetchUsers.setOnClickListener {
                viewModel.onUsersRequested()
            }

            buttonIssContinuous.setOnClickListener {
                viewModel.onFetchContinuousIssPosition()
            }

            buttonIssContinuous.setOnLongClickListener {
                viewModel.onFetchIssLongClick()
            }

            buttonIss.setOnClickListener{
                viewModel.onFetchIssPosition()
            }
        }
    }

    override fun onBackPressed() {
        when {
            binding.mainGroup.isVisible -> super.onBackPressed()
            else -> binding.viewModel?.revert()
        }
    }

}
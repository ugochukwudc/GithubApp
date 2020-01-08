package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.enums.LoadingState
import com.example.myapplication.models.IssData
import com.example.myapplication.models.RepoGson
import com.example.myapplication.models.UserGson
import com.example.myapplication.service.GithubService
import com.example.myapplication.service.IssService
import com.example.myapplication.util.intrinsicTry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger

class TestViewModel(context: Context) : ViewModel() {

    private val api: GithubService = GithubService(context)
    private val issService: IssService = IssService()

    val userName: MutableLiveData<CharSequence> = MutableLiveData("")
    val isValid: LiveData<Boolean> = map(userName) { it.isNotEmpty() }
    private val mLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.NOT_STARTED)
    private val mItems: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val listItems: LiveData<List<String>> = mItems
    val loadingState: LiveData<LoadingState> = mLoadingState
    private val mCallExceptions : MutableLiveData<Throwable> = MutableLiveData()
    val errorLiveData: LiveData<Throwable> = mCallExceptions

    fun onReposRequested() {
        viewModelScope.launch(Dispatchers.IO) {
            mLoadingState.postValue(LoadingState.STARTED)
            val repos: List<RepoGson>? = intrinsicTry(mCallExceptions) {
                api.fetchUserRepositories(userName.value.toString()).let { response ->
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Logger.getAnonymousLogger()
                            .log(Level.SEVERE, "Request for Repos not successful")
                        null
                    }
                }
            }
            updateLoadingState(repos != null)
            updateItems(repos?.map { repo ->
                "Repos:: " +
                        "name: ${repo.name} \n" +
                        "id: ${repo.id} \n" +
                        "description: ${repo.description} \n" +
                        "wasForked: ${repo.fork}"
            })
        }
    }

    fun onMore(){
        viewModelScope.launch {
            mLoadingState.postValue(LoadingState.STARTED)
            val items = intrinsicTry(mCallExceptions) {
                api.fetchUserAndRepos(userName.value.toString())
            }
            updateItems(items)
        }
    }

    fun onUsersRequested() {
        viewModelScope.launch(Dispatchers.IO) {
            mLoadingState.postValue(LoadingState.STARTED)
            val users: List<UserGson>? = intrinsicTry(mCallExceptions) {
                api.fetchUsers().let { response ->
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.d(this::class.java.simpleName, "Fetch Users failed, response = $response")
                        Logger.getAnonymousLogger()
                            .log(Level.SEVERE, "Request for Users not successful")
                        null
                    }
                }
            }
            updateLoadingState(users != null)
            updateItems(users?.map { user ->
                "User:: " +
                        "name: ${user.login} \n" +
                        "id: ${user.id} \n" +
                        "type: ${user.type} \n" +
                        "isAdmin: ${user.siteAdmin}"
            })
        }

    }

    fun onFetchIssPosition() {

        mLoadingState.postValue(LoadingState.STARTED)
        issService.fetchPosition().enqueue(object : Callback<IssData> {

            override fun onFailure(call: Call<IssData>, t: Throwable) {
                Log.e(this::class.java.simpleName, "Fetch Users failed", t)
            }

            override fun onResponse(call: Call<IssData>, response: Response<IssData>) {
                if (response.isSuccessful) {
                    Log.d(this::class.java.simpleName, "Success!!! \n $response")
                    addItem(response.body().toString())
                    updateLoadingState(true)
                } else {
                    Log.e(this::class.java.simpleName, "Response not successful")
                    Logger.getAnonymousLogger()
                        .log(Level.SEVERE, "Request for Users not successful")
                    updateLoadingState(false)
                }
            }

        })
    }

    fun onFetchContinuousIssPosition() {
        viewModelScope.launch {
            mLoadingState.postValue(LoadingState.STARTED)

            val result: IssData? = try {
                issService.getPosition().let { response ->
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Logger.getAnonymousLogger()
                            .log(Level.SEVERE, "Request for Users not successful")
                        null
                    }
                }
            } catch (e: Throwable) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.toString())
                Log.e(this::class.java.simpleName, "Fetch Users failed", e)
                null
            }

            delay(5_000)
            updateLoadingState(result != null)

            result?.let { updateItems(listOf(it.toString())) }

            if (result != null) {
                // Refresh every 10 seconds
                while (true) {
                    delay(10_000L)
                    issService.getPosition().let { response ->
                        if (response.isSuccessful && response.body() != null) {
                            addItem(response.body().toString())
                        }
                    }
                }
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun onFetchIssLongClick(): Boolean {
        viewModelScope.launch {
            mLoadingState.postValue(LoadingState.STARTED)

            // clear list items
            mItems.postValue(emptyList())

            intrinsicTry(mCallExceptions) {
                issService.fetchPositionInfinite(10_000L)
                    .collect {
                        if (it.isSuccessful) {
                            addItem(it.body().toString())
                        } else {
                            Log.e(this::class.java.simpleName, "Fetch Iss continuous failed")
                        }
                    }
            }
        }

        return true
    }

    private fun updateItems(list: List<String>?) {
        updateLoadingState(list != null)
        list?.let(mItems::postValue)
    }

    private fun addItem(s: String) {
        updateItems((mItems.value?: emptyList()) + s)
    }

    private fun updateLoadingState(isComplete: Boolean) {
        val state = if (isComplete) {
            LoadingState.COMPLETE
        } else {
            LoadingState.ERROR
        }
        mLoadingState.postValue(state)
    }

    fun revert() {
        if (viewModelScope.isActive) {
            viewModelScope.coroutineContext.cancelChildren(
                CancellationException("User Action - Back Button Pressed")
            )
        }

        mLoadingState.postValue(LoadingState.NOT_STARTED)
    }
}

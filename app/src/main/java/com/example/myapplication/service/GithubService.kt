package com.example.myapplication.service

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class GithubService(context: Context) {
    private val mClient: GithubApi = GithubApi.create(context)

    suspend fun fetchUsers() = mClient.fetchUsers()

    suspend fun fetchUserRepositories(user: String) = mClient.fetchRepos(user)

    suspend fun fetchUserDetails(user: String) = mClient.fetchUser(user)

    suspend fun fetchUserAndRepos(user: String): List<String> {
        with(CoroutineScope(Dispatchers.IO)){
            val userDetails = async { fetchUserDetails(user) }
            val repos = async { fetchUserRepositories(user) }
            return listOf(userDetails.await().body()?.toString() ?:"null User")
                .plus(repos.await().body()?.map { it.toString() } ?: emptyList())
        }
    }
}
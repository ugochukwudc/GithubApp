package com.example.myapplication.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.example.myapplication.enums.LoadingState

open class LoadingViewModel(initialState: LoadingState = LoadingState.NOT_STARTED) : ViewModel() {
    private val mState: MutableLiveData<LoadingState> = MutableLiveData(initialState)
    val loadingState: LiveData<LoadingState> = mState
    val isLoading: LiveData<Boolean> = map(mState) { it == LoadingState.STARTED }

    protected fun setLoadingState(state: LoadingState) {
        mState.postValue(state)
    }
}
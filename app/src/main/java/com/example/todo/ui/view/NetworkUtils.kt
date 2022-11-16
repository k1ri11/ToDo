package com.example.todo.ui.view

import androidx.lifecycle.LiveData

interface NetworkUtils {
    fun hasInternetConnection(): Boolean

    fun getNetworkLiveData(): LiveData<Boolean>
}
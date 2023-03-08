package com.udacity.project4.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.udacity.project4.authentication.FirebaseUserLiveData

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    enum class AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED,
    }


    val authenticationState = FirebaseUserLiveData().map {
        if (it != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


    class AuthViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}




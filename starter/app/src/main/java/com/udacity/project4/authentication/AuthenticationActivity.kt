package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.base.AuthViewModel
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.locationreminders.ReminderDescriptionActivity
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    //
    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_REQEST_CODE = 1001
    }

    //
    private val viewModel: AuthViewModel by lazy {
        val activity = requireNotNull(this) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, AuthViewModel.AuthViewModelFactory(activity.application)).get(AuthViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        //         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
        var button = findViewById<Button>(R.id.loginBtn)
        button.setOnClickListener {
            launchSignInFlow()
        }
        //          TODO: If the user was authenticated, send him to RemindersActivity

        observeAuthenticationState()
        //          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG,"notlogedin")
                    val intent = Intent(this, RemindersActivity::class.java)
                    startActivity(intent)
                    finish();
                }
                else -> {
                    Log.i(TAG,"notlogedin")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "${FirebaseAuth.getInstance().currentUser?.displayName}")
            } else {
                Log.i(TAG, "${response?.error?.errorCode}")
            }
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_REQEST_CODE
        )
    }
}

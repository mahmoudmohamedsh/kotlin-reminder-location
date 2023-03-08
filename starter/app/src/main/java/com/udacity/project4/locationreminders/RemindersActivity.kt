package com.udacity.project4.locationreminders

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.base.AuthViewModel
import kotlinx.android.synthetic.main.activity_reminders.*

/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_reminders)
        observeAuthenticationState()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
                return true
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this)
                Log.e("error","error logout")
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i("reminders","notlogedin")

                }
                else -> {
                    Log.i("reminders","notlogedin")
                    val intent = Intent(this, AuthenticationActivity::class.java)
                    startActivity(intent)
                    finish();
                }
            }
        })
    }
}

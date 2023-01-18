package com.udacity.project4.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.utils.startAndClear

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    var signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            listOf(
                GoogleBuilder().build(),
                EmailBuilder().build()
            )
        )
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_authentication
        )

        binding.lifecycleOwner = this

        if (FirebaseAuth.getInstance().currentUser != null) {
            RemindersActivity.start(this)
            finish()
        }


        binding.btnSignInButton.setOnClickListener {
            startActivityForResult(signInIntent, RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
                Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                RemindersActivity.start(this)
                finish()

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign error ${response?.error?.errorCode}")
                Toast.makeText(this, "Authentication error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val TAG = AuthenticationActivity::class.simpleName
        const val RESULT_CODE = 123

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AuthenticationActivity::class.java)
            context.startAndClear(starter)
        }

    }
}

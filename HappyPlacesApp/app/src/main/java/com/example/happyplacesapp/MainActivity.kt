package com.example.happyplacesapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
    }

    private fun getProviderData() {
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    private fun updateProfile() {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    private fun updateEmail() {
        val user = Firebase.auth.currentUser

        user!!.updateEmail("user@example.com")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
    }

    private fun sendEmailVerification() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun sendEmailVerificationWithContinueUrl() {
        val auth = Firebase.auth
        val user = auth.currentUser!!

        val url = "http://www.example.com/verify?uid=" + user.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.ios")
            // The default for this is populated with the current android package name.
            .setAndroidPackageName("com.example.android", false, null)
            .build()

        user.sendEmailVerification(actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

        // [END send_email_verification_with_continue_url]
        // [START localize_verification_email]
        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage()
        // [END localize_verification_email]
    }

    private fun updatePassword() {
        val user = Firebase.auth.currentUser
        val newPassword = "SOME-SECURE-PASSWORD"

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    private fun sendPasswordReset() {
        val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun deleteUser() {
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }

    private fun reauthenticate() {
        val user = Firebase.auth.currentUser!!

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
    }

    private fun signOut() {
        Firebase.auth.signOut()
    }

}
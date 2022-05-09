package com.example.happyplacesapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.happyplacesapp.MainActivity
import com.example.happyplacesapp.R
import com.example.happyplacesapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignInBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_in)

        val btn_to_sign_up = findViewById<Button>(R.id.tv_to_sign_up)
        btn_to_sign_up.setOnClickListener {
            val intent : Intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        val btn_sign_in = findViewById<Button>(R.id.btn_login)
        btn_sign_in.setOnClickListener {
            val et_login_email = findViewById<TextView>(R.id.et_login_email)
            val et_login_password = findViewById<TextView>(R.id.et_login_password)
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()
            signIn(email, password)
        }
    }

    public override fun onStart() {
        super.onStart()

        if(Firebase.auth.currentUser != null){
            goToMainActivity()
        }
    }

    private fun createAccount(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Authentication succeeded.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    Toast.makeText(baseContext, "Success!!.",
                        Toast.LENGTH_SHORT).show()
                    //goToMainActivity()
                    //updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {

    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}
package com.example.happyplacesapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.happyplacesapp.MainActivity
import com.example.happyplacesapp.R
import com.example.happyplacesapp.databinding.ActivitySignInBinding
import com.example.happyplacesapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_up)

        val btn_to_sign_in = findViewById<Button>(R.id.tv_to_sign_in)
        btn_to_sign_in.setOnClickListener {
            val intent : Intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        btn_register.setOnClickListener {
            when {
                TextUtils.isEmpty(et_register_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = et_register_email.text.toString().trim { it <= ' ' }
                    val password: String = et_register_password.text.toString().trim { it <= ' ' }
                    val confirmPassword: String =
                        et_confirm_password.text.toString().trim { it <= ' ' }

                    if (password == confirmPassword) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                OnCompleteListener<AuthResult> { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser: FirebaseUser = task.result!!.user!!

                                        Toast.makeText(
                                            this@SignUpActivity,
                                            "Your account was successfully created!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent =
                                            Intent(this@SignUpActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("email_id", email)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@SignUpActivity,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Password is not matching",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
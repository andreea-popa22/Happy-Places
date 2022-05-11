package com.example.happyplacesapp.auth

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.happyplacesapp.MainActivity
import com.example.happyplacesapp.R
import com.example.happyplacesapp.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        val strUrl = "https://happy-places-57ca4-default-rtdb.europe-west1.firebasedatabase.app/"
        database = FirebaseDatabase.getInstance(strUrl).getReference("data/users")
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign_up)

        val btn_to_sign_in = findViewById<Button>(R.id.tv_to_sign_in)
        btn_to_sign_in.setOnClickListener {
            val intent : Intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        val et_register_name = findViewById<TextView>(R.id.et_register_name)
        val et_register_phone = findViewById<TextView>(R.id.et_register_phone)
        btn_register.setOnClickListener {
            when {
                TextUtils.isEmpty(et_register_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please enter your email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_name.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please enter your name.",
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
                    val name: String = et_register_name.text.toString().trim { it <= ' ' }
                    val phone: String = et_register_phone.text.toString().trim { it <= ' ' }
                    val password: String = et_register_password.text.toString().trim { it <= ' ' }
                    val confirmPassword: String =
                        et_confirm_password.text.toString().trim { it <= ' ' }

                    if (password == confirmPassword) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                OnCompleteListener<AuthResult> { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser: FirebaseUser = task.result!!.user!!

//                                        val profileUpdates =
//                                            UserProfileChangeRequest.Builder()
//                                                .setDisplayName(name)
//                                                //.setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/fakelogisticscompany.appspot.com/o/default.png?alt=media&token=60224ebe-9bcb-45fd-8679-64b1408ec760"))
//                                                .build()
//
//                                        firebaseUser!!.updateProfile(profileUpdates)
//                                            .addOnCompleteListener { task ->
//                                                if (task.isSuccessful) {
//                                                    Log.d(TAG, "User profile updated.")
//                                                }
//                                            }

                                        addDataToFirebase(email, name, phone, firebaseUser.uid)

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
                                        intent.putExtra("user_email", email)
                                        intent.putExtra("user_name", name)
                                        intent.putExtra("user_phone", phone)
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

    private fun addDataToFirebase(email: String, name: String, phone: String, uid: String){
        database.child(uid).child("email").setValue(email)
        database.child(uid).child("name").setValue(name)
        database.child(uid).child("phone").setValue(phone)
    }
}
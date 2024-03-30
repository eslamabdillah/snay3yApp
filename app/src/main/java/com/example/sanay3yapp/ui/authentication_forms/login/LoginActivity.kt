package com.example.sanay3yapp.ui.authentication_forms.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityLoginBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.authentication_forms.choose_login_signup.Choose_login_signupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        
        loginBinding.buttonLogin.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

        val intent = Intent(this,Choose_login_signupActivity::class.java)
        loginBinding.buttonSignup.setOnClickListener {
            startActivity(intent)
        }
        auth = Firebase.auth

    }
}
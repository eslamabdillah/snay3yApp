package com.example.sanay3yapp.ui.authentication_forms.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityLoginBinding
import com.example.sanay3yapp.ui.authentication_forms.choose_login_signup.Choose_login_signupActivity

class LoginActivity : AppCompatActivity() {

    private  lateinit var  loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // go to choose_login_signup activity
        val intent = Intent(this,Choose_login_signupActivity::class.java)
        loginBinding.buttonSignup.setOnClickListener {
            startActivity(intent)
        }


    }
}
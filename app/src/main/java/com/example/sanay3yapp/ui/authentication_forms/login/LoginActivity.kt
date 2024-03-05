package com.example.sanay3yapp.ui.authentication_forms.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private  lateinit var  loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)



    }
}
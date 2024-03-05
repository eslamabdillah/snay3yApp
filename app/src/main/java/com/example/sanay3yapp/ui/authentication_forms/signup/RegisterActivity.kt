package com.example.sanay3yapp.ui.authentication_forms.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
    }
}
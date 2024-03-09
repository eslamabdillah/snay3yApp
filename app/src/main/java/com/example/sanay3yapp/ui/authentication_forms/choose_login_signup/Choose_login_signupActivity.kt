package com.example.sanay3yapp.ui.authentication_forms.choose_login_signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityChooseLoginSignupBinding

class Choose_login_signupActivity : AppCompatActivity() {
    private lateinit var choose_login_signup: ActivityChooseLoginSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choose_login_signup = ActivityChooseLoginSignupBinding.inflate(layoutInflater)
        setContentView(choose_login_signup.root)


    }
}
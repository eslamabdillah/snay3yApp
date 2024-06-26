package com.example.sanay3yapp.ui.authentication_forms.choose_login_signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityChooseLoginSignupBinding
import com.example.sanay3yapp.ui.authentication_forms.signup.SignUpClient
import com.example.sanay3yapp.ui.authentication_forms.signup.SignUpDailyWorker
import com.example.sanay3yapp.ui.authentication_forms.signup.SignUpWorker

class Choose_login_signupActivity : AppCompatActivity() {
    private lateinit var choose_login_signup: ActivityChooseLoginSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choose_login_signup = ActivityChooseLoginSignupBinding.inflate(layoutInflater)
        setContentView(choose_login_signup.root)


        choose_login_signup.buttonSignWorker.setOnClickListener {

            val intent = Intent(this, SignUpWorker::class.java)
            startActivity(intent)
        }

        choose_login_signup.buttonSignClient.setOnClickListener {

            val intent = Intent(this, SignUpClient::class.java)
            startActivity(intent)
        }

        choose_login_signup.btnAssistant.setOnClickListener {
            val intent = Intent(this, SignUpDailyWorker::class.java)
            startActivity(intent)

        }


    }
}
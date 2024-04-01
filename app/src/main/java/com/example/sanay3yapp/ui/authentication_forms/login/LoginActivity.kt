package com.example.sanay3yapp.ui.authentication_forms.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityLoginBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.authentication_forms.choose_login_signup.Choose_login_signupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Client
import dataBase.models.Worker

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)


        auth = FirebaseAuth.getInstance()

        loginBinding.buttonLogin.setOnClickListener {
            var email = loginBinding.editTextEmail.text.toString().trim()
            var password = loginBinding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            }
        }






        loginBinding.buttonSignup.setOnClickListener {
            val intent = Intent(this, Choose_login_signupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { currentUser ->
                        decideUserRole(currentUser.uid)

                    }
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun decideUserRole(uid: String) {
        DAO.getWorker(uid) { workerDoc ->
            if (workerDoc.isSuccessful && workerDoc.result.exists()) {
                // Found worker, fill session as worker
                SessionUser.worker = workerDoc.result.toObject<Worker>()!!
                SessionUser.mode = true
                SessionUser.currentUserType = "worker"
                Toast.makeText(this, "Worker in session", Toast.LENGTH_LONG).show()
                navigateToMain()

            }

            if (SessionUser.mode == false) {
                DAO.getClient(uid) { clientDoc ->
                    SessionUser.client = clientDoc.result.toObject<Client>()!!
                    SessionUser.mode = true
                    SessionUser.currentUserType = "client"
                    Toast.makeText(this, "client in session ", Toast.LENGTH_LONG).show()
                    navigateToMain()
                }

            } else {
                Toast.makeText(this, "No client or worker found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}



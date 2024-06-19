package com.example.sanay3yapp.ui.authentication_forms.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityLoginBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.admin.AdminActivity
import com.example.sanay3yapp.ui.authentication_forms.choose_login_signup.Choose_login_signupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObject
import dataBase.fireStore.DAO
import dataBase.models.Admin
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
            val email = loginBinding.editTextEmail.text.toString().trim()
            val password = loginBinding.editTextPassword.text.toString().trim()

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
        // Reset session state
        SessionUser.mode = false

        DAO.getWorker(uid) { workerDoc ->
            if (workerDoc.isSuccessful && workerDoc.result.exists()) {
                SessionUser.worker = workerDoc.result.toObject<Worker>()!!
                SessionUser.mode = true
                SessionUser.currentUserType = "worker"
                Toast.makeText(this, "Worker in session", Toast.LENGTH_LONG).show()
                navigateToMain()
            } else {
                checkClientRole(uid)
            }
        }
    }

    private fun checkClientRole(uid: String) {
        DAO.getClient(uid) { clientDoc ->
            if (clientDoc.isSuccessful && clientDoc.result.exists()) {
                SessionUser.client = clientDoc.result.toObject<Client>()!!
                SessionUser.mode = true
                SessionUser.currentUserType = "client"
                Toast.makeText(this, "Client in session", Toast.LENGTH_LONG).show()
                navigateToMain()
            } else {
                checkAdminRole(uid)
            }
        }
    }

    private fun checkAdminRole(uid: String) {
        DAO.getAdmin(uid) { adminDoc ->
            if (adminDoc.isSuccessful && adminDoc.result.exists()) {
                SessionUser.admin = adminDoc.result.toObject<Admin>()!!
                SessionUser.mode = true
                SessionUser.currentUserType = "admin"
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No user role found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
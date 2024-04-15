package com.example.sanay3yapp.ui.authentication_forms.signup


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivitySignUpAssistantBinding
import com.example.sanay3yapp.ui.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dataBase.fireStore.DAO
import dataBase.models.DailyWorker

class SignUpDailyWorker : AppCompatActivity() {
    lateinit var binding: ActivitySignUpAssistantBinding
    private lateinit var auth: FirebaseAuth

    var daily_worker = DailyWorker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpAssistantBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)


        binding.spinnerPlaces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                postion: Int,
                id: Long
            ) {
                daily_worker.city = parent?.getItemAtPosition(postion).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        binding.spinnerJobs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                postion: Int,
                id: Long
            ) {
                daily_worker.job = parent?.getItemAtPosition(postion).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.createAccount.setOnClickListener {
            //todo validation
            createUserWithEmailPassword()


        }
    }


    private fun createUserWithEmailPassword() {
        validation()
        auth.createUserWithEmailAndPassword(daily_worker.email, daily_worker.passWord)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    daily_worker.id = task.result.user?.uid.toString()
                    Constants.idForSignUp = daily_worker.id
                    Constants.workerType = "dailyWorker"
                    DAO.addNewDailyWorker(daily_worker) {

                        Toast.makeText(this, "sucess add", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, UploadPhotoActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // If user creation fails, display a message to the user.
                    // Handle the sign up error (e.g., email format invalid, password too weak)
                }
            }
    }


    private fun validation() {
        daily_worker.email = binding.email.text.toString()
        daily_worker.passWord = binding.password.text.toString()
        daily_worker.name = binding.name.text.toString()
        daily_worker.phone = binding.phone.text.toString().toLong()
        daily_worker.daily_rent = binding.dailyRent.text.toString().toInt()
    }


}
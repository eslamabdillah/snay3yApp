package com.example.sanay3yapp.ui.authentication_forms.signup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivitySignUpWorkerBinding
import com.example.sanay3yapp.ui.authentication_forms.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dataBase.fireStore.DAO
import dataBase.models.Worker
import java.util.Calendar

class SignUpWorker : AppCompatActivity() {
    lateinit var binding: ActivitySignUpWorkerBinding
    private lateinit var auth: FirebaseAuth

    var worker = Worker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpWorkerBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        binding.birthdayButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.spinnerPlaces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                postion: Int,
                id: Long
            ) {
                worker.city = parent?.getItemAtPosition(postion).toString()
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
                worker.job = parent?.getItemAtPosition(postion).toString()
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
        auth.createUserWithEmailAndPassword(worker.email, worker.passWord)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    worker.id = task.result.user?.uid.toString()
                    DAO.addNewWorker(worker) {
                        Toast.makeText(this, "sucess add", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // If user creation fails, display a message to the user.
                    // Handle the sign up error (e.g., email format invalid, password too weak)
                }
            }
    }


    private fun validation() {
        worker.email = binding.email.text.toString()
        worker.passWord = binding.password.text.toString()
        worker.name = binding.name.text.toString()
        worker.national_id = binding.idNational.text.toString().toLong()
        worker.phone = binding.phone.text.toString().toLong()
        worker.exp = binding.exp.text.toString().toInt()
    }

    private fun showDatePickerDialog() {
        // Create an instance of the calendar
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR) // Current year
        val month = calendar.get(Calendar.MONTH) // Current month
        val day = calendar.get(Calendar.DAY_OF_MONTH) // Current day

        // Create the DatePickerDialog instance
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date here
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                worker.day = selectedDay
                worker.month = selectedMonth
                worker.year = selectedYear
                worker.age = Calendar.YEAR - year
                binding.birthdayButton.text = selectedDate
            },
            year,
            month,
            day
        )

        // Show the DatePickerDialog
        dpd.show()
    }
}
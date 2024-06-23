package com.example.sanay3yapp.ui.authentication_forms.signup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivitySignUpClientBinding
import com.example.sanay3yapp.ui.authentication_forms.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dataBase.fireStore.DAO
import dataBase.models.Client
import java.util.Calendar

class SignUpClient : AppCompatActivity() {
    lateinit var binding: ActivitySignUpClientBinding
    private lateinit var auth: FirebaseAuth

    var client = Client()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpClientBinding.inflate(layoutInflater)
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
                client.city = parent?.getItemAtPosition(postion).toString()
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
        auth.createUserWithEmailAndPassword(client.email, client.passWord)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    client.id = task.result.user?.uid.toString()
                    DAO.addNewClient(client) {
                        Toast.makeText(this, "sucess add", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    val exception = task.exception
                    val errorMessage = exception?.localizedMessage ?: "User creation failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun validation() {
        client.email = binding.email.text.toString()
        client.passWord = binding.password.text.toString()
        client.name = binding.name.text.toString()
        client.phone = binding.phone.text.toString().toLong()
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
                client.day = selectedDay
                client.month = selectedMonth
                client.year = selectedYear
                client.age = Calendar.YEAR - year
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
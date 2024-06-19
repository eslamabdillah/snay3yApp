package com.example.sanay3yapp.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityAdminBinding
import com.example.sanay3yapp.ui.authentication_forms.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.InfoApp

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()



        binding.complaints.setOnClickListener {

        }

        binding.logOut.setOnClickListener {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun setupUi() {
        DAO.getInfoApp { task ->
            if (task.isSuccessful) {
                var infoApp = task.result.toObject<InfoApp>()

                binding.numberClients.text = " عدد العملاء \n ${infoApp!!.numClients}\n عميل   "
                binding.numberDailyWorkers.text =
                    " عدد الفواعلية \n ${infoApp!!.numDailyWorkers}\n فواعلى   "
                binding.numberWorkers.text = " عددالصنايعية \n ${infoApp!!.numWorkers}\n صنايعى   "
                binding.numberJobs.text = " عدد الوظايف \n ${infoApp!!.numJobs}\n وظيفة   "

            } else {

            }

        }
    }
}
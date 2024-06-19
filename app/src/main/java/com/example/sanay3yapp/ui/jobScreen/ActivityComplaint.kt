package com.example.sanay3yapp.ui.jobScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityComplaintBinding
import com.example.sanay3yapp.ui.MainActivity
import dataBase.fireStore.DAO
import dataBase.models.Complaint
import dataBase.models.Job

class ActivityComplaint : AppCompatActivity() {
    lateinit var binding: ActivityComplaintBinding
    var jobId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobId = intent.getStringExtra("JOB_ID")!!

        binding.cancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.send.setOnClickListener {
            var complaint = Complaint()
            DAO.getJob(jobId) {
                if (it.isSuccessful) {
                    var job = it.result.toObject(Job::class.java)

                    complaint.jobId = job!!.id
                    complaint.ClientId = job!!.owner
                    complaint.workerId = job.selectedWorker
                    complaint.content = binding.etComplaint.text.toString()
                    complaint.from = "client"

                    DAO.sendComplaint(complaint) {
                        if (it.isSuccessful) {
                            showAlertDialogAfterComplaint(this)
                        } else {

                        }
                    }
                } else {

                }

            }
        }
    }


}
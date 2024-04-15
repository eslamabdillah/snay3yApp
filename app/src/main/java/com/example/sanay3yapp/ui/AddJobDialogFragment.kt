package com.example.sanay3yapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sanay3yapp.databinding.FragmentAddJobBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import dataBase.fireStore.DAO
import dataBase.models.Job

class AddJobDialogFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddJobBinding
    var newJob = Job()

    // Format the date to a string 'YYYY-MM-DD'

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addJob.setOnClickListener({
            if (validation() == true) {
                intializeJob()
                addJobToDatabase()
            }
        })
    }

    private fun intializeJob() {
        newJob.name = binding.jobName.text.toString()
        newJob.details = binding.jobDetails.text.toString()
        newJob.duration = binding.jobDuration.text.toString().toInt()
        newJob.cost = binding.jobCost.text.toString().toInt()
        newJob.owner = SessionUser.client.id
        newJob.date = Timestamp.now()


    }

    private fun addJobToDatabase() {
        if (SessionUser.currentUserType == "client") {
            DAO.addJobAndUpdateClient(newJob, SessionUser.client.id) {
                Toast.makeText(context, "add job success", Toast.LENGTH_LONG)
                dialog?.dismiss()

            }
        }


    }

    private fun validation(): Boolean {
        var flag = true
        if (binding.jobName == null) {
            flag = false
        }

        if (binding.jobDetails == null) {
            flag = false
        }

        if (binding.jobCost == null) {
            flag = false
        }

        if (binding.jobDuration == null) {
            flag = false
        }
        return flag
    }
}
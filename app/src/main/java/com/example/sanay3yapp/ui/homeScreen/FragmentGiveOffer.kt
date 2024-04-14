package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sanay3yapp.databinding.FragmentGiveOfferBinding
import com.example.sanay3yapp.ui.SessionUser
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dataBase.fireStore.DAO
import dataBase.models.Offer

class FragmentGiveOffer : BottomSheetDialogFragment() {
    lateinit var binding: FragmentGiveOfferBinding
    var jobId: String? = ""
    var newOffer = Offer()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiveOfferBinding.inflate(inflater, container, false)
        jobId = arguments?.getString(FragmentGiveOffer.JOB_ID_KEY)
        Log.d("jobIId", "onCreateView jobId: $jobId")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.giveOffer.setOnClickListener {
            Log.d("jobIId", jobId!!.toString())
            if (validation() == true) {
                intializeOffer()
                DAO.addOfferInJob(jobId!!, newOffer) {
                    Toast.makeText(context, "add offer", Toast.LENGTH_LONG)
                }
                dialog?.dismiss()


            }

        }

    }

    private fun intializeOffer() {
        newOffer.id = jobId!!
        newOffer.cost = binding.offerCost.text.toString().toInt()
        newOffer.details = binding.offerDetails.text.toString()
        newOffer.duration = binding.offerDurarion.text.toString().toInt()
        newOffer.workerId = SessionUser.worker.id
        newOffer.jobId = jobId!!

    }

    private fun validation(): Boolean {
        if (binding.offerDetails.text.isEmpty()) {
            return false
        }

        if (binding.offerCost.text.isEmpty()) {
            return false
        }

        if (binding.offerDurarion.text.isEmpty()) {
            return false
        }

        return true

    }

    companion object {
        private const val JOB_ID_KEY = "jobID"

        fun newInstance(jobId: String) = FragmentGiveOffer().apply {
            arguments = Bundle().apply {
                putString(JOB_ID_KEY, jobId)
            }
        }
    }
}
package com.example.sanay3yapp.ui.jobScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentFulljobForClientBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.chat.ChatRoomActivity
import dataBase.fireStore.DAO
import dataBase.models.Job
import dataBase.models.Offer
import dataBase.models.Worker

class FragmentFullJobForClient : Fragment() {
    lateinit var binding: FragmentFulljobForClientBinding

    // Retrieve the Job object from the arguments
    val job = arguments?.getParcelable<Job>("job")

    companion object {
        fun newInstance(jobId: String): FragmentFullJobForClient {
            val fragment = FragmentFullJobForClient()
            val args = Bundle()
            args.putString("jobID", jobId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFulljobForClientBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("الشغلانة الحالية")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jobID = arguments?.getString("jobID")
        Log.d("jobScreen", jobID ?: "no job")
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollJob.visibility = View.GONE
        downloadData(jobID!!)


    }

    private fun downloadData(jobId: String) {
        DAO.getJob(jobId) { task ->
            if (task.isSuccessful) {
                var job = task.result.toObject(Job::class.java)
                setupJobUi(job)
                downloadOfferData(jobId, job!!.selectedOffer, job.name)
            } else {

            }

        }


    }

    private fun downloadOfferData(jobId: String, selectedOffer: String, clientName: String) {
        DAO.getSelectedOffer(jobId, selectedOffer) { task ->
            if (task.isSuccessful) {
                val offer = task.result.toObject(Offer::class.java)
                setupOfferUi(offer, clientName)
                downloadWorkerData(offer!!.workerId)

            } else {

            }

        }
    }

    private fun downloadWorkerData(workerId: String) {
        DAO.getWorker(workerId) { task ->
            if (task.isSuccessful) {
                val worker = task.result.toObject(Worker::class.java)
                setupWorkerUI(worker)

            } else {

            }

        }
    }

    private fun setupWorkerUI(worker: Worker?) {
        binding.detailsConfirmJob.sanayeyDetails.workerName.text = worker!!.name
        Glide.with(this)
            .load(worker.photoUrl)
            .placeholder(R.drawable.logo) // Optional placeholder
            .error(R.drawable.logo)
            .into(binding.detailsConfirmJob.sanayeyDetails.workerImage)

        binding.progressBar.visibility = View.GONE
        binding.scrollJob.visibility = View.VISIBLE

        binding.detailsConfirmJob.call.setOnClickListener {
            val contactNumber = worker.phone
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactNumber")
            }
            startActivity(dialIntent)


        }


    }

    private fun setupOfferUi(offer: Offer?, clientName: String) {
        binding.detailsConfirmJob.detailsOffer.text = offer!!.details
        binding.detailsConfirmJob.cost.text = "الميزانية : " + offer!!.cost.toString() + " حنيه"
        binding.detailsConfirmJob.duration.text = "المدة : " + offer!!.duration.toString() + " يوم "

        binding.detailsConfirmJob.chat.setOnClickListener {
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("ROOMID", offer.id)
            if (SessionUser.currentUserType == "worker") {
                intent.putExtra("ROOMNAME", offer.workerId)
            }
            if (SessionUser.currentUserType == "client") {
                intent.putExtra("ROOMNAME", clientName)

            }
            startActivity(intent)
        }
    }

    private fun setupJobUi(job: Job?) {
        binding.confirmJob.jobName.text = job!!.name
        binding.confirmJob.details.text = job!!.details
        binding.confirmJob.cost.text = job!!.cost.toString()
        binding.confirmJob.duration.text = job!!.duration.toString()
        binding.detailsConfirmJob.finishJob.setOnClickListener {
            DAO.finishJob(job.id) { task ->
                if (task.isSuccessful) {
                    DAO.finishJobForWorker(job.selectedWorker, job.id) {
                        if (it.isSuccessful) {
                            DAO.finishJobForClient(job.owner, job.id) {
                                if (it.isSuccessful) {
                                    showAlertDialog(requireContext(), job.id, job.selectedWorker)
                                } else {

                                }
                            }
                        } else {

                        }
                    }


                } else {

                }

            }
        }

        binding.detailsConfirmJob.makeComplaint.setOnClickListener {
            val intent = Intent(requireContext(), ActivityComplaint::class.java)
            intent.putExtra("JOB_ID", job.id)
            startActivity(intent)

        }


    }
}
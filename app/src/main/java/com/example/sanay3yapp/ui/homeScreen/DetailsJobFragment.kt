package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentDetailsJobBinding
import com.example.sanay3yapp.ui.Functions
import com.example.sanay3yapp.ui.SessionUser
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Job
import dataBase.models.Offer

class DetailsJobFragment : Fragment(), DialogDismissCallback {

    private lateinit var binding: FragmentDetailsJobBinding
    private lateinit var adapter: OffersAdapter
    private var jobId: String? = null
    private var job = Job()
    var enableGiveOffer: Boolean = true


    companion object {
        private const val JOB_ID_KEY = "jobID"

        fun newInstance(jobId: String) = DetailsJobFragment().apply {
            arguments = Bundle().apply {
                putString(JOB_ID_KEY, jobId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsJobBinding.inflate(inflater, container, false)
        jobId = arguments?.getString(JOB_ID_KEY)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        binding.content.visibility = View.GONE

        loadJob()
        setupView()
        setupAdapter()

        binding.mainJob.giveOffer.setOnClickListener({
            setupGiveOfferButton()
        })


    }


    private fun setupGiveOfferButton() {
        DAO.getOffersforWorker(SessionUser.worker.id) { task ->
            if (task.isSuccessful) {
                val documents = task.result.documents.toMutableList()
                documents.forEach({ offer ->
                    var currentOffer = offer.toObject<Offer>()
                    if (currentOffer!!.jobId == jobId) {
                        Toast.makeText(context, "أضفت عرض من قبل ", Toast.LENGTH_LONG).show()
                        enableGiveOffer = false
                    }


                })
            } else {

            }
            if (enableGiveOffer == true) {
                val bottomSheet = FragmentGiveOffer.newInstance(jobId!!)
                bottomSheet.setDialogDismissCallback(this)
                bottomSheet.show(childFragmentManager, "give offer")
            }


        }
    }

    private fun setupView() {
        if (SessionUser.currentUserType == "client") {
            binding.mainJob.giveOffer.isVisible = false
        }
    }


    private fun setupAdapter() {
        adapter = OffersAdapter(null)
        binding.recyclerViewOffers.adapter = adapter
        adapter.listenenr = OffersAdapter.OnTimeClickListener { offerObject ->
            val detailsFragment = DetailsOfferOfWorkerFragment.newInstance(offerObject, job)
            loadChildFragment(detailsFragment)
        }

    }

    private fun loadOffers() {
        binding.progressBarOffers.visibility = View.VISIBLE
        binding.recyclerViewOffers.visibility = View.GONE
        jobId?.let { jobid ->
            DAO.getOffersforJob(jobId!!) { task ->
                if (task.isSuccessful) {
                    val documents = task.result.documents
                    documents.forEach({ document ->
                        document.toObject<Offer>().let { job.workerOffers.add(it!!) }
                    })
                } else {

                }
                changeAdapterList()

                binding.progressBarOffers.visibility = View.GONE
                binding.recyclerViewOffers.visibility = View.VISIBLE


            }

        }
    }


    private fun loadJob() {
        jobId?.let { jobId ->
            DAO.getJob(jobId) { task ->
                if (task.isSuccessful) {
                    val document = task.result?.toObject<Job>()
                    if (document != null) {
                        job = document
                        binding.mainJob.jobName.text = document.name
                        binding.mainJob.details.text = document.details
                        binding.mainJob.cost.text = document.cost.toString()
                        binding.mainJob.duration.text = document.duration.toString()
                        binding.mainJob.date.text = Functions.convertToDate(document.date)
                        binding.progressBar.visibility = View.GONE
                        binding.content.visibility = View.VISIBLE
                        loadOffers()

                    } else {
                        // Handle the case where the document is null
                        // For example, logging an error or providing a default value
                        Log.e("loadJob", "Document was null")
                        job.name =
                            "Default Name"  // Provide a default value or continue with an appropriate action
                    }
                } else {
                    // Handle the case where task is not successful
                    Log.e("loadJob", "Failed to load job")
                }
            }
        }
    }

    fun changeAdapterList() {
// Sort the workerOffers list by date
        val sortedOffers = job.workerOffers.sortedBy { it.date }
        // Update the adapter with the sorted list
        adapter.bindList(sortedOffers.reversed().toMutableList())
    }

    fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_details_offer, childFragment)
        transaction.commit()
    }

    override fun onDialogDismissed() {
        refreshFragment()
    }

    private fun refreshFragment() {

        Toast.makeText(context, "تم اضافة العرض", Toast.LENGTH_SHORT).show()
        job.workerOffers.clear()
        binding.progressBarOffers.visibility = View.VISIBLE
        binding.recyclerViewOffers.visibility = View.GONE
        loadOffers()
    }


}
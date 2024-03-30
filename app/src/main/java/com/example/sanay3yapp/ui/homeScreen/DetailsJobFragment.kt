package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentDetailsJobBinding
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Job
import dataBase.models.Offer

class DetailsJobFragment : Fragment() {

    private lateinit var binding: FragmentDetailsJobBinding
    lateinit var bottomSheet: FragmentGiveOffer
    private lateinit var adapter: OffersAdapter
    private var jobId: String? = null
    private var job = Job()


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

        loadJob()
        loadOffers()
        setupAdapter()
        binding.giveOffer.setOnClickListener({
            bottomSheet = FragmentGiveOffer()
            bottomSheet.show(childFragmentManager, "give offer")
        })


    }

    private fun setupAdapter() {
        adapter = OffersAdapter(null)
        binding.recyclerViewOffers.adapter = adapter
        adapter.listenenr = OffersAdapter.OnTimeClickListener { offerObject ->
            val detailsFragment = DetailsOfferOfWorkerFragment.newInstance(offerObject)
            loadChildFragment(detailsFragment)
        }

    }

    private fun loadOffers() {
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

            }

        }
    }

    private fun loadJob() {
        jobId?.let { jobId ->
            DAO.getJob(jobId) { task ->
                if (task.isSuccessful) {
                    val document = task.result?.toObject<Job>()
                    job.name = document?.name!!
                    job.id = document.id
                    job.details = document.details
                    job.cost = document.cost
                    job.duration = document.duration
                } else {

                }
            }
        }
    }

    fun changeAdapterList() {
        adapter.bindList(job.workerOffers)
    }

    fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_details_offer, childFragment)
        transaction.commit()
    }


}
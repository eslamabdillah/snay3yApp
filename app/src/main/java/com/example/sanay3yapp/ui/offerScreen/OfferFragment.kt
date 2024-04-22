package com.example.sanay3yapp.ui.offerScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentMyOffersBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.homeScreen.DetailsJobFragment
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Job
import dataBase.models.Offer

class OfferFragment : Fragment() {
    lateinit var binding: FragmentMyOffersBinding
    lateinit var offerWorkerAdapter: MyOfferAdapter
    private var newJobsList = mutableListOf<Job>()
    lateinit var jobsAdapter: NewJobsClientAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOffersBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("عروضى")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (SessionUser.currentUserType) {
            "client" -> {
                setupAdapterJobs()
                downloadListJobsForClient()

            }

            "worker" -> {
                setupAdapterOffers()
                downloadListOffers()
            }
        }

    }

    private fun downloadListJobsForClient() {
        DAO.getNewJobsForClient(SessionUser.client.id) { result ->
            result.onSuccess { jobList ->
                val jobsToLoad = jobList.size
                var jobsLoaded = 0

                if (jobList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    Log.d("newJobs", "empty")

                    changeListAdapter()
                } else {
                    jobList.forEach { jobId ->
                        DAO.getJob(jobId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<Job>()?.let { job ->
                                    newJobsList.add(job)
                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                Log.d("newJobs", newJobsList.toString())
                                changeListAdapter()
                            }
                        }
                    }
                }
            }
        }
    }


    private fun changeListAdapter() {
        jobsAdapter.changeListAdapter(newJobsList)
    }


    private fun setupAdapterJobs() {
        jobsAdapter = NewJobsClientAdapter(null)
        binding.recyclerview.adapter = jobsAdapter

        jobsAdapter.listener = NewJobsClientAdapter.OnItemClickListener { job ->

            var childFragment = DetailsJobFragment.newInstance(job.id)
            loadChildFragment(childFragment)


        }


    }

    private fun downloadListOffers() {
        val workerId = SessionUser.worker.id
        DAO.getOffersforWorker(workerId) { task ->
            if (task.isSuccessful) {
                var offersListt = task.result?.toObjects(Offer::class.java) ?: emptyList()
                changerListOffers(offersListt.toMutableList())
            } else {
                Log.e("downloadList", "Error getting documents: ", task.exception)
            }
        }
    }

    private fun setupAdapterOffers() {
        offerWorkerAdapter = MyOfferAdapter(null)
        binding.recyclerview.adapter = offerWorkerAdapter
    }

    private fun changerListOffers(newList: MutableList<Offer>) {
        offerWorkerAdapter.bindList(newList)
    }

    fun loadChildFragment(childFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
            .replace(R.id.fragment_offer_tab, childFragment)
            .commit()
    }


}
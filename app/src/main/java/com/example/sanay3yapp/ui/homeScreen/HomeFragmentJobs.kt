package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentHomeJobsBinding
import com.example.sanay3yapp.ui.MainActivity
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Job

class HomeFragmentJobs : Fragment() {

    private lateinit var offerBinding: FragmentHomeJobsBinding
    private lateinit var adapter: JobsAdapter
    private var jobList = mutableListOf<Job>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        offerBinding = FragmentHomeJobsBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("الرئيسية - الوظائف")
        return offerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        downloadOffersList()
    }

    private fun downloadOffersList() {
        DAO.getAllJobs() { task ->
            if (task.isSuccessful) {
                val documents = task.result?.documents
                documents?.forEach() { document ->
                    document.toObject<Job>()?.let { jobList.add(it) }
                    Log.d("firestoreJob", "${jobList[0].name}")

                }

            } else {
                Log.e("firestoreError", "getAllOffers fun")
            }

            changeListAdapter()

        }
    }

    private fun setupRecyclerView() {
        adapter = JobsAdapter(null)
        offerBinding.recyclerViewOffers.adapter = adapter
        adapter.listener = JobsAdapter.OnItemClickListener { jobId ->
            DAO.getJob(jobId) { task ->
                val detailsFragment = DetailsJobFragment.newInstance(jobId)
                loadChildFragment(detailsFragment)
            }
        }
    }

    private fun changeListAdapter() {
        adapter.bindList(jobList)
    }

    fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_job_container, childFragment)
        transaction.commit()
    }


}
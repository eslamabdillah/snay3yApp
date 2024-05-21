package com.example.sanay3yapp.ui.jobScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentJobsBinding
import com.example.sanay3yapp.databinding.FragmentJobsClientsBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Agreement
import dataBase.models.Client
import dataBase.models.Job
import dataBase.models.Offer
import dataBase.models.Worker

class JobFragment : Fragment() {
    lateinit var binding: FragmentJobsBinding
    lateinit var bindingClient: FragmentJobsClientsBinding
    var agreement = Agreement()
    private var inWorkJobsList = mutableListOf<Job>()
    lateinit var adapter: InWorkJobClientAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (SessionUser.currentUserType == "client") {
            bindingClient = FragmentJobsClientsBinding.inflate(inflater, container, false)
            (activity as? MainActivity)?.changeFragmentTitle("وظايفك")

            return bindingClient.root


        } else {
            binding = FragmentJobsBinding.inflate(inflater, container, false)
            (activity as? MainActivity)?.changeFragmentTitle("الشغلانة الحالية")
            return binding.root


        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (SessionUser.currentUserType == "client") {
            setupRecyclerView()
            downloadData()


        } else if (SessionUser.currentUserType == "worker") {
            setupViewForWorker()


        } else {

        }


    }


    private fun setupRecyclerView() {
        adapter = InWorkJobClientAdapter(null)
        bindingClient.recyclerViewInWorkJobs.adapter = adapter
        adapter.listener = InWorkJobClientAdapter.OnItemClickListener { job ->


            var childFragment = FragmentFullJobForClient.newInstance(job.id)


            loadChildFragment(childFragment)


        }

    }


    private fun downloadData() {
        DAO.getInWorkJobArray(SessionUser.client.id) { result ->
            result.onSuccess { jobList ->
                val jobsToLoad = jobList.size
                var jobsLoaded = 0

                if (jobList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    changeListAdapter()
                } else {
                    jobList.forEach { jobId ->
                        DAO.getJob(jobId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<Job>()?.let { job ->
                                    inWorkJobsList.add(job)
                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                Log.d("inworkjobAfterLoadData", inWorkJobsList.toString())
                                changeListAdapter()
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setupViewForWorker() {
        DAO.getWorker(SessionUser.worker.id) { task ->
            if (task.isSuccessful) {
                agreement.worker = task.result.toObject<Worker>()!!
                Log.d("agreementWorkerName", agreement.worker.name)
                binding.detailsConfirmJob.sanayeyDetails.workerName.text = agreement.worker.name
                getJobForAgreement(agreement.worker.currentJob)
            } else {
                //failed
            }

        }

    }

    private fun getJobForAgreement(jobId: String) {
        DAO.getJob(jobId) { task ->
            if (task.isSuccessful) {
                agreement.job = task.result.toObject<Job>()!!
                Log.d("agreementJobName", agreement.job.name)
                binding.confirmJob.jobName.text = agreement.job.name
                binding.confirmJob.details.text = agreement.job.details
                binding.confirmJob.cost.text = agreement.job.cost.toString()
                binding.confirmJob.duration.text = agreement.job.duration.toString()
                getClientForAgreement(agreement.job.owner)
                getOfferForAgreement(agreement.job.id, agreement.job.selectedOffer)
            } else {

            }

        }

    }

    private fun getClientForAgreement(clientId: String) {
        DAO.getClient(clientId) { task ->
            if (task.isSuccessful) {
                agreement.client = task.result.toObject<Client>()!!
            } else {

            }
        }

    }

    private fun getOfferForAgreement(jobId: String, offerid: String) {
        DAO.getSelectedOffer(jobId, offerid) { task ->
            if (task.isSuccessful) {
                agreement.offer = task.result.toObject<Offer>()!!
                binding.detailsConfirmJob.detailsOffer.text = agreement.offer.details
                binding.detailsConfirmJob.cost.text =
                    "الميزانية :" + agreement.offer.cost.toString() + " جنيه"
                binding.detailsConfirmJob.duration.text =
                    "المدة :" + agreement.offer.duration.toString() + "يوم"
                binding.fullJob.isVisible = true
            } else {

            }

        }

    }

    private fun changeListAdapter() {
        adapter.changeListAdapter(inWorkJobsList)
    }


    /*fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_job_container, childFragment)
        transaction.commit()
    }*/

    fun loadChildFragment(childFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
            .replace(R.id.fragment_job_container_in_job, childFragment)
            .commit()
    }


}
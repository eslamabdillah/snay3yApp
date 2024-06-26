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
import com.example.sanay3yapp.ui.homeScreen.DetailsJobFragment
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
    private var newJobsList = mutableListOf<Job>()
    private var completeJobsList = mutableListOf<Job>()
    lateinit var newAdapter: NewJobsClientAdapter
    lateinit var completeJobAdapter: CompleteJobsAdapter
    lateinit var offersAdapter: OffersWorkerAdapter
    var inWorkAdapter = InWorkJobClientAdapter(null)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (SessionUser.currentUserType == "client") {
            bindingClient = FragmentJobsClientsBinding.inflate(inflater, container, false)
            (activity as? MainActivity)?.changeFragmentTitle("متابعة الشغلانات")
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

            bindingClient.progressBar.visibility = View.VISIBLE
            bindingClient.recyclerViewInWorkJobs.visibility = View.GONE
            bindingClient.txtNotJobs.visibility = View.GONE



            bindingClient.nowJobs.setOnClickListener {
                bindingClient.txtNotJobs.visibility = View.GONE
                bindingClient.progressBar.visibility = View.VISIBLE
                bindingClient.recyclerViewInWorkJobs.visibility = View.GONE

                bindingClient.compelteJob.setBackgroundResource(R.drawable.button_job_fragment)
                bindingClient.nowJobs.setBackgroundResource(R.drawable.button_job_fragment_agree)
                bindingClient.newJobs.setBackgroundResource(R.drawable.button_job_fragment)
                setupRecyclerViewInWork()
                inWorkJobsList.clear()
                downloadData()
            }
            bindingClient.newJobs.setOnClickListener {
                bindingClient.progressBar.visibility = View.VISIBLE
                bindingClient.recyclerViewInWorkJobs.visibility = View.GONE
                bindingClient.txtNotJobs.visibility = View.GONE

                bindingClient.compelteJob.setBackgroundResource(R.drawable.button_job_fragment)
                bindingClient.nowJobs.setBackgroundResource(R.drawable.button_job_fragment)
                bindingClient.newJobs.setBackgroundResource(R.drawable.button_job_fragment_agree)
                newJobsList.clear()
                setupAdapterNewJobs()
                downloadListJobsForClient()


            }

            bindingClient.compelteJob.setOnClickListener {
                bindingClient.progressBar.visibility = View.VISIBLE
                bindingClient.recyclerViewInWorkJobs.visibility = View.GONE
                bindingClient.txtNotJobs.visibility = View.GONE

                bindingClient.compelteJob.setBackgroundResource(R.drawable.button_job_fragment_agree)
                bindingClient.nowJobs.setBackgroundResource(R.drawable.button_job_fragment)
                bindingClient.newJobs.setBackgroundResource(R.drawable.button_job_fragment)
                completeJobsList.clear()
                setupAdapterCompleteJobs()
                downloadCompleteJobsData()
            }

            bindingClient.newJobs.performClick()


        } else if (SessionUser.currentUserType == "worker") {
            binding.progressBar.visibility = View.VISIBLE
            binding.workerRecyclerView.visibility = View.GONE
            binding.txtNotJobs.visibility = View.GONE
            binding.restJobScreen.visibility = View.GONE
            binding.detailsConfirmJob.finishJob.visibility = View.GONE

            binding.nowJobs.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                binding.workerRecyclerView.visibility = View.GONE
                binding.txtNotJobs.visibility = View.GONE

                binding.compelteJob.setBackgroundResource(R.drawable.button_job_fragment)
                binding.nowJobs.setBackgroundResource(R.drawable.button_job_fragment_agree)
                binding.offers.setBackgroundResource(R.drawable.button_job_fragment)

                DAO.getWorker(SessionUser.worker.id) { task ->
                    if (task.isSuccessful) {
                        val worker = task.result.toObject(Worker::class.java)
                        if (worker!!.currentJob == "" || worker.currentJob == null
                            || worker.currentJob.isEmpty()
                        ) {
                            binding.restJobScreen.visibility = View.GONE
                            binding.workerRecyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.txtNotJobs.visibility = View.VISIBLE

                        } else {

                            setupViewForWorker()
                        }

                    } else {

                    }

                }


            }

            binding.compelteJob.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                binding.workerRecyclerView.visibility = View.GONE
                binding.txtNotJobs.visibility = View.GONE




                binding.compelteJob.setBackgroundResource(R.drawable.button_job_fragment_agree)
                binding.nowJobs.setBackgroundResource(R.drawable.button_job_fragment)
                binding.offers.setBackgroundResource(R.drawable.button_job_fragment)
                completeJobsList.clear()

                setupAdapterCompleteJobsForWorker()
                downloadCompleteJobsDataForWorker()


            }

            binding.offers.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                binding.workerRecyclerView.visibility = View.GONE
                binding.txtNotJobs.visibility = View.GONE

                binding.compelteJob.setBackgroundResource(R.drawable.button_job_fragment)
                binding.nowJobs.setBackgroundResource(R.drawable.button_job_fragment)
                binding.offers.setBackgroundResource(R.drawable.button_job_fragment_agree)

                setupAdapterOffers()
                downloadListOffers()
            }

            binding.offers.performClick()


        } else {

        }


    }


    private fun setupRecyclerViewInWork() {
        inWorkAdapter = InWorkJobClientAdapter(null)
        bindingClient.recyclerViewInWorkJobs.adapter = inWorkAdapter
        inWorkAdapter.listener = InWorkJobClientAdapter.OnItemClickListener { job ->


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
                    bindingClient.txtNotJobs.visibility = View.VISIBLE
                    bindingClient.progressBar.visibility = View.GONE
                    bindingClient.recyclerViewInWorkJobs.visibility = View.GONE

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
                //binding.progressBar.isVisible=false

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
                binding.restJobScreen.visibility = View.VISIBLE
                binding.workerRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.txtNotJobs.visibility = View.GONE
            } else {

            }

        }

    }

    private fun changeListAdapter() {


        if (inWorkJobsList.isEmpty()) {
            inWorkAdapter.changeListAdapter(inWorkJobsList)
            bindingClient.progressBar.visibility = View.GONE
            bindingClient.recyclerViewInWorkJobs.visibility = View.GONE
            bindingClient.txtNotJobs.visibility = View.VISIBLE
        } else {
            inWorkAdapter.changeListAdapter(inWorkJobsList)
            bindingClient.progressBar.visibility = View.GONE
            bindingClient.recyclerViewInWorkJobs.visibility = View.VISIBLE
            bindingClient.txtNotJobs.visibility = View.GONE
        }

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

    //new jobs functions
    private fun setupAdapterNewJobs() {
        newAdapter = NewJobsClientAdapter(null)
        bindingClient.recyclerViewInWorkJobs.adapter = newAdapter

        newAdapter.listener = NewJobsClientAdapter.OnItemClickListener { job ->

            var childFragment = DetailsJobFragment.newInstance(job.id)
            loadChildFragment(childFragment)


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
                    bindingClient.progressBar.visibility = View.GONE
                    bindingClient.txtNotJobs.visibility = View.VISIBLE

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
                                changeListAdapterNewJobs()
                                bindingClient.progressBar.visibility = View.GONE
                                bindingClient.recyclerViewInWorkJobs.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun changeListAdapterNewJobs() {
        newAdapter.changeListAdapter(newJobsList)
    }

    //complete job
    private fun setupAdapterCompleteJobs() {
        completeJobAdapter = CompleteJobsAdapter(null)
        bindingClient.recyclerViewInWorkJobs.adapter = completeJobAdapter

        completeJobAdapter.listener = CompleteJobsAdapter.OnItemClickListener { job ->

            var childFragment = DetailsJobFragment.newInstance(job.id)
            loadChildFragment(childFragment)


        }


    }


    //dede
    private fun changeListAdapterCompleteJobs() {

        if (completeJobsList.isEmpty()) {
            completeJobAdapter.changeListAdapter(completeJobsList)
            bindingClient.progressBar.visibility = View.GONE
            bindingClient.recyclerViewInWorkJobs.visibility = View.GONE
            bindingClient.txtNotJobs.visibility = View.VISIBLE
        } else {
            completeJobAdapter.changeListAdapter(completeJobsList)
            bindingClient.progressBar.visibility = View.GONE
            bindingClient.recyclerViewInWorkJobs.visibility = View.VISIBLE
            bindingClient.txtNotJobs.visibility = View.GONE
        }


    }

    private fun changeListAdapterCompleteJobsForWorker() {

        if (completeJobsList.isEmpty()) {
            completeJobAdapter.changeListAdapter(completeJobsList)

            binding.restJobScreen.visibility = View.GONE
            binding.workerRecyclerView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.txtNotJobs.visibility = View.VISIBLE
        } else {

            completeJobAdapter.changeListAdapter(completeJobsList)
            binding.restJobScreen.visibility = View.GONE
            binding.workerRecyclerView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.txtNotJobs.visibility = View.GONE
        }


    }

    private fun downloadCompleteJobsData() {
        DAO.getCompleteJobArray(SessionUser.client.id) { result ->
            result.onSuccess { jobList ->
                val jobsToLoad = jobList.size
                var jobsLoaded = 0

                if (jobList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    changeListAdapterCompleteJobs()
                    bindingClient.progressBar.visibility = View.GONE
                    bindingClient.txtNotJobs.visibility = View.VISIBLE
                    bindingClient.recyclerViewInWorkJobs.visibility = View.GONE

                } else {
                    jobList.forEach { jobId ->
                        DAO.getJob(jobId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<Job>()?.let { job ->
                                    completeJobsList.add(job)
                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                Log.d("inworkjobAfterLoadData", inWorkJobsList.toString())
                                changeListAdapterCompleteJobs()

                            }
                        }
                    }
                }
            }
        }
    }

    //completejobsfor Worker
    private fun setupAdapterCompleteJobsForWorker() {
        completeJobAdapter = CompleteJobsAdapter(null)
        binding.workerRecyclerView.adapter = completeJobAdapter

        completeJobAdapter.listener = CompleteJobsAdapter.OnItemClickListener { job ->

            var childFragment = DetailsJobFragment.newInstance(job.id)
            loadChildFragment(childFragment)


        }


    }

    private fun downloadCompleteJobsDataForWorker() {
        DAO.getCompleteJobArrayForWorker(SessionUser.worker.id) { result ->
            result.onSuccess { jobList ->
                val jobsToLoad = jobList.size
                var jobsLoaded = 0

                if (jobList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    changeListAdapterCompleteJobsForWorker()
                    binding.restJobScreen.visibility = View.GONE
                    binding.workerRecyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.txtNotJobs.visibility = View.VISIBLE


                } else {
                    jobList.forEach { jobId ->
                        DAO.getJob(jobId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<Job>()?.let { job ->
                                    completeJobsList.add(job)

                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                Log.d("inworkjobAfterLoadData", inWorkJobsList.toString())
                                binding.restJobScreen.isVisible = false
                                changeListAdapterCompleteJobsForWorker()
                            }
                        }
                    }
                }
            }
        }
    }


    // new offers for worker
    private fun setupAdapterOffers() {
        offersAdapter = OffersWorkerAdapter(null)
        binding.workerRecyclerView.adapter = offersAdapter
        offersAdapter.listener = OffersWorkerAdapter.OnItemClickListener { offer ->
            val detailsJobFragment = DetailsJobFragment.newInstance(offer.jobId)
            val transaction = childFragmentManager.beginTransaction()
                .replace(R.id.fullJob, detailsJobFragment)
                .commit()

        }
    }

    private fun downloadListOffers() {
        val workerId = SessionUser.worker.id
        DAO.getOffersforWorker(workerId) { task ->
            if (task.isSuccessful) {
                var offersListt = task.result?.toObjects(Offer::class.java) ?: emptyList()
                if (offersListt.isEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    binding.txtNotJobs.visibility = View.VISIBLE

                } else {
                    changerListOffers(offersListt.toMutableList())
                    binding.workerRecyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

            } else {
                Log.e("downloadList", "Error getting documents: ", task.exception)
            }
        }
    }

    private fun changerListOffers(newList: MutableList<Offer>) {

        newList.sortByDescending { it.date }


        offersAdapter.bindList(newList)
    }


}
package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentWorkerDetailsBinding
import com.example.sanay3yapp.ui.profileScreen.FragmentClientsOpinions
import com.example.sanay3yapp.ui.profileScreen.FragmentGallery
import dataBase.fireStore.DAO
import dataBase.models.Worker

class DetailsWorkerFragment : Fragment() {
    lateinit var binding: FragmentWorkerDetailsBinding
    var workerId = ""

    companion object {
        fun newInstance(workerID: String): DetailsWorkerFragment {
            val args = Bundle().apply {
                putString("workerId", workerID)
            }
            val fragment = DetailsWorkerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkerDetailsBinding.inflate(inflater, container, false)
        workerId = arguments?.getString("workerId") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkerData()
    }

    private fun setupScreen(worker: Worker) {
        Glide.with(this)
            .load(worker.photoUrl)
            .placeholder(R.drawable.logo) // Optional placeholder
            .error(R.drawable.logo)
            .into(binding.workerImage)

        binding.name.text = worker.name
        binding.city.text = worker.city
        binding.exp.text = worker.exp.toString()
        binding.job.text = worker.job
        binding.ratingBar.rating = worker.rate
        binding.clientsOpinions.setOnClickListener {
            val fragment = FragmentClientsOpinions.newInstance(worker.id)
            loadChildFragment(fragment)
        }

        binding.workerGallery.setOnClickListener {
            val fragment = FragmentGallery.newInstance(worker.id)

            loadChildFragment(fragment)
        }


    }

    fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.worker_details_container, childFragment)
        transaction.commit()
    }

    private fun getWorkerData() {
        DAO.getWorker(workerId) { task ->
            if (task.isSuccessful) {
                var worker = task.result.toObject(Worker::class.java)
                setupScreen(worker!!)

            } else {

            }

        }
    }
}
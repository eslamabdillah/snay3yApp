package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentHomeWorkersBinding
import dataBase.fireStore.DAO
import dataBase.models.Worker

class HomeFragmentWorker : Fragment() {

    private lateinit var workerBinding: FragmentHomeWorkersBinding
    private lateinit var workerAdapter: WorkerAdapter
    var workerList = mutableListOf<Worker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        workerBinding = FragmentHomeWorkersBinding.inflate(inflater, container, false)
        return workerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workerBinding.searchContainer.visibility = View.GONE
        setupRecyclerView()
        fillWorkersList()


    }

    private fun fillWorkersList() {
        DAO.getAllWorkers { task ->
            if (task.isSuccessful) {
                workerList = task.result.toObjects(Worker::class.java)
                onBindWorkersList()

            } else {

            }

        }

    }


    private fun setupRecyclerView() {
        workerAdapter = WorkerAdapter(null)
        workerBinding.recyclerView.adapter = workerAdapter
        workerAdapter.listener = WorkerAdapter.OnItemClickListener { workerId ->
            DAO.getWorker(workerId) { task ->
                val detailsFragment = DetailsWorkerFragment.newInstance(workerId)
                loadChildFragment(detailsFragment)
            }
        }
    }

    private fun onBindWorkersList() {
        workerAdapter.bindList(workerList)
    }

    fun loadChildFragment(childFragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.details_worker_container, childFragment)
        transaction.commit()
    }


}
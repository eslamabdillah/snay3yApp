package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentHomeWorkersBinding
import com.example.sanay3yapp.ui.MainActivity
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
        (activity as? MainActivity)?.changeFragmentTitle("الرئيسية - الصنايعية")

        return workerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun onBindWorkersList() {
        workerAdapter.bindList(workerList)
    }


}
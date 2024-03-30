package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentHomeWorkersBinding
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.Worker

class HomeFragmentWorker : Fragment() {

    private lateinit var workerBinding: FragmentHomeWorkersBinding
    private val workerDao = DAO
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

        setupRecyclerView()
        fillWorkersList()


    }

    private fun fillWorkersList() {
        workerDao.getAllWorkers { task ->
            if (task.isSuccessful) {
                // The query was successful, process the data
                val documents = task.result?.documents
                documents?.forEach { document ->
                    try {
                        document.toObject<Worker>()?.let { workerList.add(it) }
                        Log.d("FirestoreLog", "Worker added: ${workerList[0].name}")
                    } catch (e: Exception) {
                        Log.e("FirestoreConversion", "Error converting document to Worker", e)
                    }
                }
                // Now that workerList is populated, bind it to the RecyclerView
                Log.d("FirestoreLog", "Worker check after added: ${workerList[0].name}")
                try {
                    onBindWorkersList()
                } catch (e: Exception) {
                    Log.e("FirestoreLog3", "Error to bind", e)

                }
            } else {
                // The query failed, handle the exception
                val exception = task.exception
                Log.e("FirestoreLog", "Error fetching documents", exception)
            }
        }
        // Do not access workerList here, it won't be populated yet!
    }


    private fun setupRecyclerView() {
        workerAdapter = WorkerAdapter(null)
        workerBinding.recyclerView.adapter = workerAdapter
    }

    private fun onBindWorkersList() {
        workerAdapter.bindList(workerList)
    }


}
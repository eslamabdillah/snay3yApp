package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentHomeWorkersBinding
import com.example.sanay3yapp.ui.MainActivity
import dataBase.fireStore.DAO
import dataBase.models.Worker

class HomeFragmentWorker : Fragment() {

    private lateinit var workerBinding: FragmentHomeWorkersBinding
    private lateinit var workerAdapter: WorkerAdapter
    private var selectedJob : String = ""
    private var selectedPlace : String = ""
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

    private fun jobFilter(){
        workerBinding.spinnerJobs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item

                selectedJob = parent.getItemAtPosition(position) as String
                Log.d("selected job",selectedJob)


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected, if needed
            }
        }
    }



    private fun fillWorkersList() {
        workerBinding.spinnerJobs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item
                workerBinding.spinnerPlaces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        // Get the selected item
                        selectedPlace = parent.getItemAtPosition(position) as String
                        DAO.getWorkerByField(selectedJob,selectedPlace) { task ->
                            Log.d("job",selectedJob)
                            if (task.isSuccessful) {
                                workerList = task.result.toObjects(Worker::class.java)
                                onBindWorkersList()

                            } else {

                            }

                        }
                        Log.d("selected place",selectedPlace)


                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Handle case where nothing is selected, if needed
                    }
                }

                selectedJob = parent.getItemAtPosition(position) as String
                DAO.getWorkerByField(selectedJob,selectedPlace) { task ->
                    Log.d("job",selectedJob)
                    if (task.isSuccessful) {
                        workerList = task.result.toObjects(Worker::class.java)
                        onBindWorkersList()

                    } else {

                    }

                }
                Log.d("selected job",selectedJob)


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected, if needed
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
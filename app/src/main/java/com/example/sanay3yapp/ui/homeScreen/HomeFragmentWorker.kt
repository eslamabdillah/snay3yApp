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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dataBase.fireStore.DAO
import dataBase.models.Worker

class HomeFragmentWorker : Fragment() {
    private val db = Firebase.firestore
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

    private fun fillWorkersList() {
        workerBinding.spinnerJobs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedJob = parent.getItemAtPosition(position) as String
                setSpinnerPlacesListener()
                fetchWorkers(selectedJob, selectedPlace)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(parent.context, "No job selected", Toast.LENGTH_SHORT).show()
                selectedJob = ""
            }
        }
    }

    private fun setSpinnerPlacesListener() {
        workerBinding.spinnerPlaces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPlace = parent.getItemAtPosition(position) as String
                fetchWorkers(selectedJob, selectedPlace)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(parent.context, "No place selected", Toast.LENGTH_SHORT).show()
                selectedPlace = ""
            }
        }
    }

    private fun fetchWorkers(job: String, place: String) {
        when {
            place == "كل المحافظات" && job == "كل الصنايعية" -> {
                DAO.getAllWorkers { task ->
                    handleTaskResult(task)
                }
            }
            place == "كل المحافظات" -> {
                getWorkerByFieldAnyPlace(job) { task ->
                    handleTaskResult(task)
                }
            }
            job == "كل الصنايعية" -> {
                getWorkerByFieldAnyJob(place) { task ->
                    handleTaskResult(task)
                }
            }
            else -> {
                getWorkerByField(job, place) { task ->
                    handleTaskResult(task)
                }
            }
        }
    }

    private fun handleTaskResult(task: Task<QuerySnapshot>) {
        if (task.isSuccessful) {
            workerList = task.result.toObjects(Worker::class.java)
            onBindWorkersList()
        } else {
            // Handle error
        }
    }

    private fun fillWorkersLists() {
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

    fun getWorkerByField(workerJob: String, workerCity: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        val docRef = db.collection("workers")
            .whereEqualTo("job", workerJob)
            .whereEqualTo("city", workerCity)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getWorkerByFieldAnyPlace(workerJob: String,  onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        val docRef = db.collection("workers")
            .whereEqualTo("job", workerJob)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    fun getWorkerByFieldAnyJob( workerCity: String, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        val docRef = db.collection("workers")
            .whereEqualTo("city", workerCity)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }


}
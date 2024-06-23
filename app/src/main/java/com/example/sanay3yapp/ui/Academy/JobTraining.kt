package com.example.sanay3yapp.ui.Academy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sanay3yapp.databinding.ActivityJobTrainingBinding
import com.example.sanay3yapp.ui.homeScreen.WorkerAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dataBase.fireStore.DAO
import dataBase.models.Worker

class JobTraining : AppCompatActivity() {

    val db = Firebase.firestore
    lateinit var jobTrainingBinding: ActivityJobTrainingBinding
    private lateinit var workerAdapter: WorkerAdapter
    var workerList = mutableListOf<Worker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jobTrainingBinding = ActivityJobTrainingBinding.inflate(layoutInflater)
        setContentView(jobTrainingBinding.root)

        val workerType = intent.getStringExtra("WORKER_TYPE")

        jobTrainingBinding.workerTypeId.text = workerType


        setupRecyclerView()
        fillWorkersList()
    }

    fun getAllWorkers(onCompleteListener: OnCompleteListener<QuerySnapshot>) {

        val workerJob = intent.getStringExtra("WORKER_JOB")
        DAO.db.collection("workers").whereEqualTo("job",workerJob)
            .get()
            .addOnCompleteListener(onCompleteListener)

    }

    private fun fillWorkersList() {
        getAllWorkers { task ->
            if (task.isSuccessful) {
                workerList = task.result.toObjects(Worker::class.java)
                onBindWorkersList()

            } else {

            }

        }

    }

    private fun setupRecyclerView() {
        workerAdapter = WorkerAdapter(null)
        jobTrainingBinding.recyclerView.adapter = workerAdapter
    }

    private fun onBindWorkersList() {
        workerAdapter.bindList(workerList)
    }
}
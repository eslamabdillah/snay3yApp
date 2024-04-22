package com.example.sanay3yapp.ui.dailyScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentDailyBinding
import com.example.sanay3yapp.ui.MainActivity
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.DailyWorker

class DailyFragment : Fragment() {
    lateinit var dailyBinding: FragmentDailyBinding
    lateinit var dailyAdapter: DailyAdapter
    var assistantsList = mutableListOf<DailyWorker>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyBinding = FragmentDailyBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("مساعدين وفواعلية")

        return dailyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        downloadDailyWorkerList()
    }

    private fun downloadDailyWorkerList() {
        DAO.getAllDailyWorkers() { task ->
            if (task.isSuccessful) {
                val documents = task.result.documents
                documents.forEach() { document ->
                    document.toObject<DailyWorker>()?.let { assistantsList.add(it) }

                }

            } else {
                Log.e("firebaseError", "getAllDailyWorkers")
            }
            changeDailyAdapterList()

        }
    }

    private fun setupRecycler() {
        dailyAdapter = DailyAdapter(null)
        dailyBinding.recyclerViewDaily.adapter = dailyAdapter
    }

    fun changeDailyAdapterList() {
        dailyAdapter.bindNewList(assistantsList)
    }
}
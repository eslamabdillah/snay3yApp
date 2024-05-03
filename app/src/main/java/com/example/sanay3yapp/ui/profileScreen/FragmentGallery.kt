package com.example.sanay3yapp.ui.profileScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentGalleryBinding
import com.example.sanay3yapp.ui.SessionUser
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.GallaryProject

class FragmentGallery : Fragment() {
    lateinit var binding: FragmentGalleryBinding
    lateinit var adapter: ProjectsAdapter
    private var projectsList = mutableListOf<GallaryProject>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        downloadProjectForWorker()


        binding.addProject.setOnClickListener {
            val intent = Intent(requireContext(), AddProjectActivity::class.java)
            startActivity(intent)
        }

    }

    private fun downloadProjectForWorker() {
        DAO.getProjectForWorker(SessionUser.worker.id) { task ->
            if (task.isSuccessful) {
                // Optionally clear the list to avoid duplicates
                projectsList.clear()

                val documents = task.result?.documents
                documents?.forEach { document ->
                    document.toObject<GallaryProject>()?.let { project ->
                        projectsList.add(project)
                    }
                }
                // Notify change after updating the list
                changeAdapterList()

            } else {
                Log.e("firestoreError", "getAllOffers fun")
            }


        }
    }

    private fun setupAdapter() {
        adapter = ProjectsAdapter(null)
        binding.rvProjects.adapter = adapter
    }

    private fun changeAdapterList() {
        adapter.changeAdapterList(projectsList)

    }
}
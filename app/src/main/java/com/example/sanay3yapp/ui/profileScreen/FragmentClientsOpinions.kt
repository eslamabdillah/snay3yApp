package com.example.sanay3yapp.ui.profileScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentClientsOpinionsBinding
import com.example.sanay3yapp.ui.SessionUser
import dataBase.fireStore.DAO
import dataBase.models.ClientOpinion

class FragmentClientsOpinions : Fragment() {
    lateinit var binding: FragmentClientsOpinionsBinding
    lateinit var adapter: OpinionsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientsOpinionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        downloadData()

    }

    private fun downloadData() {
        DAO.getClientOpinionForWorker(SessionUser.worker.id) { task ->
            if (task.isSuccessful) {
                val list = task.result.toObjects(ClientOpinion::class.java)
                changeAdapterList(list)

                Toast.makeText(context, "done", Toast.LENGTH_LONG)
            } else {
                Toast.makeText(
                    context,
                    "error happen :" + { task.exception.toString() },
                    Toast.LENGTH_LONG)
            }

        }
    }

    private fun changeAdapterList(list: MutableList<ClientOpinion>) {
        adapter.changeAdapterList(list)

    }

    private fun setupAdapter() {
        adapter = OpinionsAdapter()
        binding.recyclerViewOpinions.adapter = adapter
    }


}
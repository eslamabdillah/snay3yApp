package com.example.sanay3yapp.ui.offerScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentMyOffersBinding
import com.example.sanay3yapp.ui.SessionUser
import dataBase.fireStore.DAO
import dataBase.models.Offer

class OfferFragment : Fragment() {
    lateinit var offersBinding: FragmentMyOffersBinding
    lateinit var adapter: MyOfferAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        offersBinding = FragmentMyOffersBinding.inflate(inflater, container, false)
        return offersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        downloadList()
    }

    private fun downloadList() {
        val workerId = SessionUser.worker.id
        DAO.getOffersforWorker(workerId) { task ->
            if (task.isSuccessful) {
                var offersListt = task.result?.toObjects(Offer::class.java) ?: emptyList()
                changerList(offersListt.toMutableList())
            } else {
                Log.e("downloadList", "Error getting documents: ", task.exception)
            }
        }
    }

    private fun setupAdapter() {
        adapter = MyOfferAdapter(null)
        offersBinding.recyclerview.adapter = adapter
    }

    private fun changerList(newList: MutableList<Offer>) {
        adapter.bindList(newList)
    }


}
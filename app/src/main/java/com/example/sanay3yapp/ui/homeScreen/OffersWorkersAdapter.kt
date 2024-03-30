package com.example.sanay3yapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.Offer

class OffersWorkersAdapter(var offerList: MutableList<Offer>?) :
    RecyclerView.Adapter<OffersWorkersAdapter.OfferWorkerVH>() {
    class OfferWorkerVH(offerItem: View) : ViewHolder(offerItem) {
        var details: TextView = offerItem.findViewById(R.id.details)
        var cost: TextView = offerItem.findViewById(R.id.cost)
        var duration: TextView = offerItem.findViewById(R.id.duration)
        fun bindData(offerObject: Offer) {
            details.text = offerObject.details
            cost.text = offerObject.cost.toString()
            duration.text = offerObject.duration.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferWorkerVH {
        val offerViewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_worker_offer, parent, false)
        return OfferWorkerVH(offerViewItem)
    }

    override fun getItemCount(): Int {
        return offerList?.size ?: 0
    }

    override fun onBindViewHolder(holder: OfferWorkerVH, position: Int) {
        var currentView = offerList!![position]
        holder.bindData(currentView)

    }

    fun changeListAdapter(newList: MutableList<Offer>) {
        offerList = newList

    }
}
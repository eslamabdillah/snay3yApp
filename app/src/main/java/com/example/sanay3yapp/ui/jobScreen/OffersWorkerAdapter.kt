package com.example.sanay3yapp.ui.jobScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.Offer

class OffersWorkerAdapter(var offersList: MutableList<Offer>? = null) :
    RecyclerView.Adapter<OffersWorkerAdapter.MyOffersViewHolder>() {

    class MyOffersViewHolder(var view: View) : ViewHolder(view) {
        var offerCost: TextView = view.findViewById(R.id.offer_cost)
        var offerDetails: TextView = view.findViewById(R.id.offer_details)
        var offerDuration: TextView = view.findViewById(R.id.offer_duration)
        fun bindData(newOffer: Offer) {
            offerCost.text = newOffer.cost.toString()
            offerDetails.text = newOffer.details
            offerDuration.text = newOffer.duration.toString()


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOffersViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_offer, parent, false)
        return MyOffersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return offersList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyOffersViewHolder, position: Int) {
        var currentOffer = offersList!![position]
        holder.bindData(currentOffer)
        if (listener != null) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(currentOffer)
            }
        }
    }

    fun bindList(newList: MutableList<Offer>) {
        offersList = newList
        notifyDataSetChanged()
    }

    var listener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(offer: Offer)
    }


}
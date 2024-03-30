package com.example.sanay3yapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.Job

class JobsAdapter(var offerList: MutableList<Job>?) :
    RecyclerView.Adapter<JobsAdapter.OfferViewHolder>() {
    class OfferViewHolder(view: View) : ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val details: TextView = view.findViewById(R.id.details)
        private val cost: TextView = view.findViewById(R.id.cost)
        private val duration: TextView = view.findViewById(R.id.duration)

        fun bindData(offer: Job) {
            name.text = offer.name
            details.text = offer.details
            cost.text = offer.cost.toString()
            duration.text = offer.duration.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val offerItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(offerItem)
    }

    override fun getItemCount(): Int {
        return offerList?.size ?: 0
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        var currentOffer = offerList!![position]
        holder.bindData(currentOffer)
        if (listener != null) {
            holder.itemView.setOnClickListener() {
                listener?.onItemClick(currentOffer.id)
            }
        }

    }

    fun bindList(newList: MutableList<Job>) {
        offerList = newList
        notifyDataSetChanged()
    }

    //make onItem click listener

    var listener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(id: String)
    }

}
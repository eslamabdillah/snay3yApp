package com.example.sanay3yapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.Offer

class OffersAdapter(var offersList: MutableList<Offer>?) :
    RecyclerView.Adapter<OffersAdapter.OfferVH>() {
    class OfferVH(val view: View) : ViewHolder(view) {
        var details: TextView = view.findViewById(R.id.details)
        var cost: TextView = view.findViewById(R.id.cost)
        var duration: TextView = view.findViewById(R.id.duration)
        fun bindData(offer: Offer) {
            details.text = offer.details
            cost.text = offer.cost.toString()
            duration.text = offer.duration.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_worker_offer, parent, false)
        return OfferVH(view)
    }

    override fun getItemCount(): Int = offersList?.size ?: 0


    override fun onBindViewHolder(holder: OfferVH, position: Int) {
        val currentOffer = offersList!![position]
        holder.bindData(currentOffer)
        if (listenenr != null) {
            holder.itemView.setOnClickListener() {
                listenenr?.onItemClick(currentOffer)
            }
        }
    }

    fun bindList(list: MutableList<Offer>) {
        offersList = list
        notifyDataSetChanged()
    }

    var listenenr: OnTimeClickListener? = null

    fun interface OnTimeClickListener {
        fun onItemClick(offer: Offer)

    }
}
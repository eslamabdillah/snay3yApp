package com.example.sanay3yapp.ui.homeScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import dataBase.fireStore.DAO
import dataBase.models.Offer
import dataBase.models.Worker

class OffersAdapter(var offersList: MutableList<Offer>?) :
    RecyclerView.Adapter<OffersAdapter.OfferVH>() {
    val contextp: Context? = null

    class OfferVH(val view: View, private val context: Context) : ViewHolder(view) {
        var photo: ImageView = view.findViewById(R.id.worker_photo)
        var details: TextView = view.findViewById(R.id.details)
        var cost: TextView = view.findViewById(R.id.cost)
        var duration: TextView = view.findViewById(R.id.duration)
        fun bindData(offer: Offer) {
            DAO.getWorker(offer.workerId) { task ->
                if (task.isSuccessful) {
                    var worker = task.result.toObject(Worker::class.java)
                    worker?.photoUrl?.let { url ->
                        Glide.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo) // Optional placeholder
                            .error(R.drawable.logo)      // Fallback image
                            .into(photo)
                    }

                }

            }
            details.text = offer.details
            cost.text = offer.cost.toString()
            duration.text = offer.duration.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferVH {
        val context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_worker_offer, parent, false)
        return OfferVH(view, context)
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
package com.example.sanay3yapp.ui.profileScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.ClientOpinion

class OpinionsAdapter(var opinionsList: MutableList<ClientOpinion>? = null) :
    RecyclerView.Adapter<OpinionsAdapter.OpinionsVH>() {
    class OpinionsVH(var view: View) : ViewHolder(view) {
        var rate_quality: RatingBar = view.findViewById(R.id.rating_quality)
        var rate_delivery: RatingBar = view.findViewById(R.id.rating_delivery)
        var rate_workAgain: RatingBar = view.findViewById(R.id.rating_workAgain)
        var opinionText: TextView = view.findViewById(R.id.client_opinion)
        var jobName: TextView = view.findViewById(R.id.job_name)

        fun bindData(opinion: ClientOpinion) {
            rate_quality.rating = opinion.rateQuality
            rate_delivery.rating = opinion.rateDelivery
            rate_workAgain.rating = opinion.rateWorkAgain
            opinionText.text = opinion.opinionText
            jobName.text = opinion.jobName


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpinionsVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_client_opinion, parent, false)
        return OpinionsVH(view)
    }

    override fun getItemCount(): Int {
        return opinionsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: OpinionsVH, position: Int) {
        var currentOpinion = opinionsList!![position]
        holder.bindData(currentOpinion)
    }

    fun changeAdapterList(newList: MutableList<ClientOpinion>) {
        opinionsList = newList
        notifyDataSetChanged()
    }


}
package com.example.sanay3yapp.ui.offerScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.Job

class NewJobsClientAdapter(var inWorkJobList: MutableList<Job>? = null) :
    RecyclerView.Adapter<NewJobsClientAdapter.InWorkJobViewHolder>() {
    class InWorkJobViewHolder(val view: View) : ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.job_name)
        private val details: TextView = view.findViewById(R.id.details)
        private val cost: TextView = view.findViewById(R.id.cost)
        private val duration: TextView = view.findViewById(R.id.duration)

        fun bindData(currentJob: Job) {
            name.text = currentJob.name
            details.text = currentJob.details
            cost.text = currentJob.cost.toString()
            duration.text = currentJob.duration.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InWorkJobViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return InWorkJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return inWorkJobList?.size ?: 0
    }

    override fun onBindViewHolder(holder: InWorkJobViewHolder, position: Int) {
        var currentJob = inWorkJobList!![position]
        holder.bindData(currentJob)

        if (listener != null) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(currentJob)

            }

        }
    }

    fun changeListAdapter(newList: MutableList<Job>?) {
        inWorkJobList = newList
        notifyDataSetChanged()

    }

    var listener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(job: Job)

    }
}
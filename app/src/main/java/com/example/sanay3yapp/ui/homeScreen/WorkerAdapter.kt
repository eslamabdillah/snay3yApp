package com.example.sanay3yapp.ui.homeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sanay3yapp.R
import dataBase.models.Worker

class WorkerAdapter(private var workersList: MutableList<Worker>?) :
    RecyclerView.Adapter<WorkerAdapter.workerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): workerViewHolder {
        val workerItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_worker, parent, false)
        return workerViewHolder(workerItem)
    }

    override fun getItemCount(): Int {
        return workersList?.size ?: 0
    }

    override fun onBindViewHolder(holder: workerViewHolder, position: Int) {
        val workerOb = workersList!![position]
        holder.bind(workerOb)
    }

    class workerViewHolder(workerItem: View) : RecyclerView.ViewHolder(workerItem) {
        private var name: TextView = workerItem.findViewById(R.id.name_worker_item)
        private var place: TextView = workerItem.findViewById(R.id.place_worker_item)
        private var job: TextView = workerItem.findViewById(R.id.job)
        fun bind(worker: Worker) {
            name.text = worker.name
            place.text = worker.city
            job.text = worker.job
        }
    }

    fun bindList(newList: MutableList<Worker>?) {
        workersList = newList
        notifyDataSetChanged()
    }
}
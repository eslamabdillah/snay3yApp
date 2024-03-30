package com.example.sanay3yapp.ui.dailyScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.DailyWorker

class DailyAdapter(var workerList: MutableList<DailyWorker>?) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    class DailyViewHolder(view: View) : ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val age: TextView = view.findViewById(R.id.age)
        private val place: TextView = view.findViewById(R.id.place)
        private val salary: TextView = view.findViewById(R.id.salary)

        fun bindData(dailyWorker: DailyWorker) {
            name.text = dailyWorker.name
            age.text = dailyWorker.age.toString()
            place.text = dailyWorker.place
            salary.text = dailyWorker.salary.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_worker, parent, false)
        return DailyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workerList?.size ?: 0
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        var currentWorker = workerList!![position]
        holder.bindData(currentWorker)
    }

    fun bindNewList(newList: MutableList<DailyWorker>) {
        workerList = newList
        notifyDataSetChanged()
    }

}
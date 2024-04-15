package com.example.sanay3yapp.ui.dailyScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import dataBase.models.DailyWorker

class DailyAdapter(var workerList: MutableList<DailyWorker>?) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    class DailyViewHolder(var view: View, private val context: Context) : ViewHolder(view) {
        private val photo: ImageView = view.findViewById(R.id.worker_photo)
        private val name: TextView = view.findViewById(R.id.name)
        private val place: TextView = view.findViewById(R.id.place)
        private val salary: TextView = view.findViewById(R.id.salary)
        private val call: Button = view.findViewById(R.id.call)

        fun bindData(dailyWorker: DailyWorker) {
            name.text = dailyWorker.name
            place.text = dailyWorker.city
            salary.text = dailyWorker.daily_rent.toString() + "  جنيه"
            Glide.with(context)
                .load(dailyWorker.photoUrl)
                .placeholder(R.drawable.logo) // Optional placeholder
                .error(R.drawable.logo)
                .into(photo)

            call.setOnClickListener {
                val contactNumber = "01033242661"
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$contactNumber")
                }
                context.startActivity(dialIntent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        var currentContext = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_worker, parent, false)
        return DailyViewHolder(view, currentContext)
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
package com.example.sanay3yapp.ui.offerScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import dataBase.models.ChatRoom

class ChatAdapter(var chatroomList: MutableList<ChatRoom>? = null) :
    RecyclerView.Adapter<ChatAdapter.RoomViewHolder>() {
    class RoomViewHolder(view: View) : ViewHolder(view) {
        private val nameJob: TextView = view.findViewById(R.id.job_name_chat)


        fun bindData(chatRoom: ChatRoom) {
            nameJob.text = chatRoom.jobName
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatroomList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        var currentChatRoom = chatroomList!![position]
        holder.bindData(currentChatRoom)
        if (listener != null) {
            holder.itemView.setOnClickListener {
                listener?.onItemClick(currentChatRoom)
            }
        }

    }

    fun changeListAdapter(newList: MutableList<ChatRoom>?) {
        chatroomList = newList
        notifyDataSetChanged()

    }

    var listener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(chatRoom: ChatRoom)
    }
}
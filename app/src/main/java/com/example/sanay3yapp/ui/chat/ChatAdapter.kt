package com.example.sanay3yapp.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sanay3yapp.R
import com.example.sanay3yapp.ui.Functions
import com.example.sanay3yapp.ui.SessionUser
import dataBase.models.Message

class ChatAdapter(var list: MutableList<Message>? = null) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(var view: View) : ViewHolder(view) {
        private var send_message: TextView = view.findViewById(R.id.txt_send_message)
        private var received_message: TextView = view.findViewById(R.id.txt_received_message)
        private var sendMessageLayout: LinearLayout = view.findViewById(R.id.send_message)
        private var receivedMessageLayout: LinearLayout = view.findViewById(R.id.received_message)
        private var date: TextView = view.findViewById(R.id.txt_send_date)

        fun bindDataForSend(message: Message) {
            receivedMessageLayout.isVisible = false
            send_message.text = message.content
            date.text = Functions.convertToDateToMessage(message.time)

        }

        fun bindDateForReceived(message: Message) {
            sendMessageLayout.isVisible = false
            received_message.text = message.content
            date.text = Functions.convertToDateToMessage(message.time)

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var itemMessage =
            LayoutInflater.from(parent.context).inflate(R.layout.item_massege, parent, false)
        return ChatViewHolder(itemMessage)


    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var currentMessage = list!![position]
        if (currentMessage.ownerType == SessionUser.currentUserType) {
            holder.bindDataForSend(currentMessage)
        } else {
            holder.bindDateForReceived(currentMessage)
        }
    }

    fun addMessage(message: Message) {
        list!!.add(message)
        notifyItemInserted(list!!.size - 1)
    }

    fun changeAdapterList(newMessages: MutableList<Message>) {
        list = newMessages
        notifyDataSetChanged()
    }


}

package com.example.sanay3yapp.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sanay3yapp.databinding.ActivityChatRoomBinding
import com.example.sanay3yapp.ui.SessionUser
import dataBase.fireStore.DAO
import dataBase.models.Client
import dataBase.models.Message
import dataBase.models.Worker

class ChatRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    lateinit var adapter: ChatAdapter
    var roomId = ""
    var roomName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        roomId = intent.getStringExtra("ROOMID") ?: ""
        roomName = intent.getStringExtra("ROOMNAME") ?: "اسم"

        if (SessionUser.currentUserType == "client") {
            roomName = getWorkerName(roomName)
        }

        if (SessionUser.currentUserType == "worker") {
            roomName = getClientName(roomName)
        }

        binding.chatTitle.text = roomName

        setupAdapter()

        DAO.getChatRoom(roomId, adapter)

        binding.btnSend.setOnClickListener {
            var message = Message()
            message.content = binding.etMessage.text.toString()

            if (SessionUser.currentUserType == "worker") {
                message.ownerType = "worker"
                message.ownerId = SessionUser.worker.id

            }
            if (SessionUser.currentUserType == "client") {
                message.ownerType = "client"
                message.ownerId = SessionUser.client.id
            }

            DAO.sendMessage(message, roomId) {

            }

            binding.etMessage.text.clear()
            hideKeyboard()


        }


    }

    private fun setupAdapter() {
        adapter = ChatAdapter(mutableListOf())
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getWorkerName(clientId: String): String {
        var client = Client() ?: null
        DAO.getClient(clientId) { task ->
            if (task.isSuccessful) {
                client = task.result.toObject(Client::class.java)

            } else {

            }


        }

        return client?.name ?: "اسم"
    }

    private fun getClientName(workerId: String): String {
        var worker = Worker() ?: null
        DAO.getWorker(workerId) { task ->
            if (task.isSuccessful) {
                worker = task.result.toObject(Worker::class.java)
            } else {

            }

        }
        return worker?.name ?: "اسم"
    }
}
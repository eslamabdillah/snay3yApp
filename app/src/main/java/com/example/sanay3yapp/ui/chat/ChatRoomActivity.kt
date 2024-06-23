package com.example.sanay3yapp.ui.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityChatRoomBinding
import com.example.sanay3yapp.ui.SessionUser
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.ChatRoom
import dataBase.models.Client
import dataBase.models.Job
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

        fillAppBar()

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

    private fun fillAppBar() {
        DAO.getChatRoom(roomId) { chatRoom ->
            if (chatRoom.isSuccessful) {
                val currentRoom = chatRoom.result.toObject(ChatRoom::class.java)
                DAO.getJob(currentRoom!!.jobId) { task ->
                    if (task.isSuccessful) {
                        val currentJob = task.result.toObject<Job>()
                        binding.jobTitle.text = "الوظيفة : ${currentJob!!.name}"
                        if (SessionUser.currentUserType == "client") {
                            DAO.getWorker(currentRoom!!.workerId) { task ->
                                if (task.isSuccessful) {
                                    val worker = task.result.toObject(Worker::class.java)
                                    binding.chatTitle.text = worker!!.name
                                    Glide.with(this)
                                        .load(SessionUser.worker.photoUrl)
                                        .placeholder(R.drawable.logo) // Optional placeholder
                                        .error(R.drawable.logo)
                                        .into(binding.chatImage)

                                } else {

                                }


                            }

                        }

                        if (SessionUser.currentUserType == "worker") {
                            DAO.getClient(currentRoom!!.clientId) { task ->
                                if (task.isSuccessful) {
                                    val client = task.result.toObject(Client::class.java)
                                    binding.chatTitle.text = client!!.name
                                    Log.d("personName", client.name)

                                } else {

                                }


                            }

                        }
                    } else {

                    }

                }


            } else {

            }

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


}
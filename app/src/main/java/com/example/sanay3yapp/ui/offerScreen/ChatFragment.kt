package com.example.sanay3yapp.ui.offerScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentChatRoomsBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.chat.ChatRoomActivity
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.ChatRoom

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatRoomsBinding
    private var chatrooms = mutableListOf<ChatRoom>()
    lateinit var chatAdapter: ChatAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomsBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("الرسائل")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        if (SessionUser.currentUserType == "client") {
            downloadDataForClient()

        }
        if (SessionUser.currentUserType == "worker") {
            downloadDataForWorker()


        }


    }


    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(null)
        binding.recyclerview.adapter = chatAdapter
        chatAdapter.listener = ChatAdapter.OnItemClickListener { chatRoom ->
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("ROOMID", chatRoom.id)
            if (SessionUser.currentUserType == "worker") {
                intent.putExtra("ROOMNAME", chatRoom.workerId)
            }
            if (SessionUser.currentUserType == "client") {
                intent.putExtra("ROOMNAME", chatRoom.clientId)

            }
            startActivity(intent)


        }

    }

    private fun downloadDataForClient() {
        DAO.getChatRoomsArrayForClient(SessionUser.client.id) { result ->
            result.onSuccess { roomsList ->
                val jobsToLoad = roomsList.size
                var jobsLoaded = 0

                if (roomsList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    changeListAdapterCompleteJobs()
                } else {
                    roomsList.forEach { roomId ->
                        DAO.getChatRoom(roomId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<ChatRoom>()?.let { room ->
                                    chatrooms.add(room)
                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                changeListAdapterCompleteJobs()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun downloadDataForWorker() {
        DAO.getChatRoomsArrayForWorker(SessionUser.worker.id) { result ->
            result.onSuccess { roomsList ->
                val jobsToLoad = roomsList.size
                var jobsLoaded = 0

                if (roomsList.isEmpty()) {
                    // No jobs to load, update UI accordingly
                    changeListAdapterCompleteJobs()
                } else {
                    roomsList.forEach { roomId ->
                        DAO.getChatRoom(roomId) { task ->
                            if (task.isSuccessful) {
                                task.result.toObject<ChatRoom>()?.let { room ->
                                    chatrooms.add(room)
                                }
                            } else {
                                Log.e("JobLoadError", "Failed to load job")
                            }

                            jobsLoaded++
                            if (jobsLoaded == jobsToLoad) {
                                // Only update the adapter when all jobs have been loaded
                                changeListAdapterCompleteJobs()
                            }
                        }
                    }
                }
            }
        }
    }


    fun loadChildFragment(childFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
            .replace(R.id.fragment_offer_tab, childFragment)
            .commit()
    }

    private fun changeListAdapterCompleteJobs() {
        chatAdapter.changeListAdapter(chatrooms)
    }


}
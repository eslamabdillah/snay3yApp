package com.example.sanay3yapp.ui.homeScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentDetailsWorkerOfferBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.chat.ChatRoomActivity
import com.example.sanay3yapp.ui.jobScreen.MessageDialogFragment
import dataBase.fireStore.DAO
import dataBase.models.ChatRoom
import dataBase.models.Job
import dataBase.models.Offer

class DetailsOfferOfWorkerFragment : Fragment(), MessageDialogFragment.OnButtonClickListener {
    lateinit var binding: FragmentDetailsWorkerOfferBinding
    lateinit var currentOffer: Offer
    lateinit var currentJob: Job
    var chatroom = ChatRoom()

    companion object {
        private const val receive_offer = "receive_offer"
        private const val receive_job = "receive_job"
        fun newInstance(offer: Offer, job: Job): DetailsOfferOfWorkerFragment {
            val fragment = DetailsOfferOfWorkerFragment()
            val args = Bundle().apply {
                putParcelable("receive_offer", offer)
                putParcelable("receive_job", job)

            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsWorkerOfferBinding.inflate(inflater, container, false)
        val offer = arguments?.getParcelable<Offer>(receive_offer)
        val job = arguments?.getParcelable<Job>(receive_job)
        currentOffer = offer!!
        currentJob = job!!
        if (SessionUser.currentUserType == "worker") {
            binding.employ.isVisible = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsOffer.text = currentOffer?.details
        binding.cost.text = "الميزانية : ${currentOffer?.cost.toString()}"
        binding.duration.text = "المدة : ${currentOffer?.duration.toString()}"

        binding.progressBar.visibility = View.GONE

        binding.call.setOnClickListener {
            val contactNumber = "01033242661"
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactNumber")
            }
            startActivity(dialIntent)
        }

        binding.employ.setOnClickListener {
            //تفحص بعد مواققة العمل تتغير حالة الوظيفة
            //توضع فى الوظايف الحالية لكل من العامل والمستخدم
            //تحمل بعد ذلك لكل منهم
            binding.progressBar.visibility = View.VISIBLE
            Log.d("currentJob id", currentJob.id)
            DAO.setConfirmedJob(currentOffer.id, currentOffer.workerId, currentJob.id) { fullTask ->
                if (fullTask.isSuccessful) {
                    Log.d("finally4", "reach to finally")

                    DAO.updateConfirmJobForWorker(currentOffer.workerId, currentJob.id)
                    { workerTask ->
                        if (workerTask.isSuccessful) {
                            Log.d("finally3", "reach to finally")

                            DAO.updateConfirmJobForClient(
                                currentJob.owner,
                                currentJob.id
                            ) { clientTask ->
                                if (clientTask.isSuccessful) {
                                    Log.d("finally2", "reach to finally")

                                    binding.progressBar.visibility = View.GONE
                                    val dialog = MessageDialogFragment()
                                    dialog.show(childFragmentManager, "تم توظيف العامل بنحاح")

                                } else {
                                    //clientTask failed
                                }

                            }
                        } else {
                            //worker task failed
                        }
                    }
                } else {

                }


            }

        }


        binding.chat.setOnClickListener {
            Log.d("ChatButton", "Button clicked")

            val chatroom = ChatRoom().apply {
                id = currentOffer.id
                clientId = currentJob.owner
                workerId = currentOffer.workerId
                jobId = currentJob.id
                jobName = currentJob.name

            }

            // Create the chat room
            DAO.makeChatRoom(chatroom) { chatRoomResult ->
                if (chatRoomResult.isSuccessful) {
                    // Store chat room with client
                    DAO.storeChatRoomWithClient(
                        chatroom.id,
                        SessionUser.client.id
                    ) { clientResult ->
                        if (clientResult.isSuccessful) {
                            // Store chat room with worker
                            DAO.storeChatRoomWithWorker(
                                chatroom.id,
                                currentOffer.workerId
                            ) { workerResult ->
                                if (workerResult.isSuccessful) {
                                    // Prepare and start the chat room activity
                                    val intent =
                                        Intent(requireContext(), ChatRoomActivity::class.java)
                                    intent.putExtra("ROOMID", currentOffer.id)
                                    if (SessionUser.currentUserType == "worker") {
                                        intent.putExtra("ROOMNAME", currentOffer.workerId)
                                    }
                                    if (SessionUser.currentUserType == "client") {
                                        intent.putExtra("ROOMNAME", currentJob.owner)

                                    }
                                    startActivity(intent)
                                } else {
                                    Log.e("ChatButton", "Failed to store chat room with worker")
                                    // Handle the error case
                                }
                            }
                        } else {
                            Log.e("ChatButton", "Failed to store chat room with client")
                            // Handle the error case
                        }
                    }
                } else {
                    Log.e("ChatButton", "Failed to create chat room")
                    // Handle the error case
                }
            }
        }
        binding.sanayeyDetails.workerDetails.setOnClickListener {
            val fragment = DetailsWorkerFragment.newInstance(currentOffer.workerId)
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_worker_Details, fragment)
                .commit()
        }

    }

    override fun onButtonClick() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }


}
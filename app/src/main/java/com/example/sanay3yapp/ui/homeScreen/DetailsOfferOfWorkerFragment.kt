package com.example.sanay3yapp.ui.homeScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentDetailsWorkerOfferBinding
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.chat.ChatRoomActivity
import dataBase.fireStore.DAO
import dataBase.models.ChatRoom
import dataBase.models.Job
import dataBase.models.Offer

class DetailsOfferOfWorkerFragment : Fragment() {
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
            Log.d("currentJob id", currentJob.id)
            DAO.setConfirmedJob(currentOffer.id, currentOffer.workerId, currentJob.id) { fullTask ->
                if (fullTask.isSuccessful) {
                    DAO.updateConfirmJobForWorker(
                        currentOffer.workerId,
                        currentJob.id
                    ) { workerTask ->
                        if (workerTask.isSuccessful) {
                            DAO.updateConfirmJobForClient(
                                currentJob.owner,
                                currentJob.id
                            ) { clientTask ->
                                if (clientTask.isSuccessful) {
                                    Toast.makeText(context, "sucess", Toast.LENGTH_LONG)
                                } else {
                                    //clientTask failed
                                }

                            }
                        } else {
                            //worker task failed
                        }
                    }

                } else {
                    //task for confirmed job failed
                }

            }


        }

        binding.chat.setOnClickListener {
            Log.d("ChatButton", "Button clicked")

            chatroom.id = currentOffer.id
            chatroom.clientId = currentJob.owner
            chatroom.workerId = currentOffer.workerId
            chatroom.jobId = currentJob.id
            DAO.makeChatRoom(chatroom) {
                val intent = Intent(requireContext(), ChatRoomActivity::class.java)
                intent.putExtra("ROOMID", currentOffer.id)
                if (SessionUser.currentUserType == "worker") {
                    intent.putExtra("ROOMNAME", currentOffer.workerId)
                }
                if (SessionUser.currentUserType == "client") {
                    intent.putExtra("ROOMNAME", currentJob.owner)

                }
                startActivity(intent)
            }


        }

    }


}
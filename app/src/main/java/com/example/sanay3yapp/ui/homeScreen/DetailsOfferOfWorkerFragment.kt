package com.example.sanay3yapp.ui.homeScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentDetailsWorkerOfferBinding
import dataBase.models.Offer

class DetailsOfferOfWorkerFragment : Fragment() {
    lateinit var binding: FragmentDetailsWorkerOfferBinding
    var currectOffer = Offer() ?: null

    companion object {
        private const val ARG_OFFER = "offer"

        fun newInstance(offer: Offer): DetailsOfferOfWorkerFragment {
            val fragment = DetailsOfferOfWorkerFragment()
            val args = Bundle().apply {
                putParcelable(ARG_OFFER, offer)
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
        val offer = arguments?.getParcelable<Offer>(ARG_OFFER)
        currectOffer = offer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsOffer.text = currectOffer?.details

        binding.call.setOnClickListener {
            val contactNumber = "01033242661"
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactNumber")
            }
            startActivity(dialIntent)
        }

    }
}
package com.example.sanay3yapp.ui.offerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentMyOffersBinding

class OfferFragment : Fragment() {
    lateinit var offersBinding: FragmentMyOffersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        offersBinding = FragmentMyOffersBinding.inflate(inflater, container, false)
        return offersBinding.root
    }
}
package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentHomeOffersBinding

class HomeFragmentOffers : Fragment() {

    private lateinit var offerBinding: FragmentHomeOffersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        offerBinding = FragmentHomeOffersBinding.inflate(inflater, container, false)
        return offerBinding.root
    }


}
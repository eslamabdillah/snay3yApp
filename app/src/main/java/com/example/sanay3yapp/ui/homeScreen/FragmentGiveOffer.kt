package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sanay3yapp.databinding.FragmentGiveOfferBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentGiveOffer : BottomSheetDialogFragment() {
    lateinit var binding: FragmentGiveOfferBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiveOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
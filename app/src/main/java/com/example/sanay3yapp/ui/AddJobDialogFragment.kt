package com.example.sanay3yapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sanay3yapp.databinding.FragmentAddJobBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddJobDialogFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddJobBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addJob.setOnClickListener({
            if (validation() == true) {
                addJobToDatabase()
            }
        })
    }

    private fun addJobToDatabase() {

    }

    private fun validation(): Boolean {
        TODO("Not yet implemented")
    }
}
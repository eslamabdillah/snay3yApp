package com.example.sanay3yapp.ui.jobScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentFulljobForClientBinding
import com.example.sanay3yapp.ui.MainActivity

class FragmentFullJobForClient : Fragment() {
    lateinit var binding: FragmentFulljobForClientBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFulljobForClientBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("الشغلانة الحالية")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
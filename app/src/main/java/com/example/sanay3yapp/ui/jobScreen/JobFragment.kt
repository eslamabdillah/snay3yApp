package com.example.sanay3yapp.ui.jobScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentJobsBinding

class JobFragment : Fragment() {
    lateinit var jobBinding: FragmentJobsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jobBinding = FragmentJobsBinding.inflate(inflater, container, false)
        return jobBinding.root
    }
}
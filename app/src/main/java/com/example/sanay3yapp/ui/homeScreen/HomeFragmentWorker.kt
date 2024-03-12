package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentHomeWorkersBinding

class HomeFragmentWorker : Fragment() {

    private lateinit var workerBinding: FragmentHomeWorkersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        workerBinding = FragmentHomeWorkersBinding.inflate(inflater, container, false)
        return workerBinding.root
    }


}
package com.example.sanay3yapp.ui.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var homeFragmentBinding: FragmentHomeBinding? = null
    private val binding get() = homeFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, HomeFragmentWorker())
            .commit()

        homeFragmentBinding!!.workers.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, HomeFragmentWorker())
                .commit()
        }

        homeFragmentBinding!!.offers.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, HomeFragmentOffers())
                .commit()
        }
    }


}
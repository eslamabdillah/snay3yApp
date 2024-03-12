package com.example.sanay3yapp.ui.dailyScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.databinding.FragmentDailyBinding

class DailyFragment : Fragment() {
    lateinit var dailyBinding: FragmentDailyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyBinding = FragmentDailyBinding.inflate(inflater, container, false)
        return dailyBinding.root
    }
}
package com.example.sanay3yapp.ui.Academy

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.databinding.ActivityAcademyBinding

class AcademyActivity : AppCompatActivity() {
    lateinit var binding: ActivityAcademyBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivityAcademyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
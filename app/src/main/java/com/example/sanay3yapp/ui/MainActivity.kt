package com.example.sanay3yapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityMainBinding
import com.example.sanay3yapp.ui.dailyScreen.DailyFragment
import com.example.sanay3yapp.ui.homeScreen.HomeFragment
import com.example.sanay3yapp.ui.jobScreen.JobFragment
import com.example.sanay3yapp.ui.offerScreen.OfferFragment
import com.example.sanay3yapp.ui.profileScreen.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Assuming you have a BottomNavigationView with id bottomTabs in your layout
        mainBinding.bottomTabs.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFragmentSelected(HomeFragment())
                    true
                }

                R.id.navigation_job -> {
                    showFragmentSelected(JobFragment())
                    true
                }

                R.id.navigation_profile -> {
                    showFragmentSelected(ProfileFragment())
                    true
                }

                R.id.navigation_offer -> {
                    showFragmentSelected(OfferFragment())
                    true
                }

                R.id.navigation_daily -> {
                    showFragmentSelected(DailyFragment())
                    true
                }


                else -> false
            }
        }

        // Set default selected item if needed
        mainBinding.bottomTabs.selectedItemId = R.id.navigation_home

        //add job

    }

    private fun showFragmentSelected(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
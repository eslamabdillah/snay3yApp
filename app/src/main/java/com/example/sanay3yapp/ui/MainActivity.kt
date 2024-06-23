package com.example.sanay3yapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityMainBinding
import com.example.sanay3yapp.ui.dailyScreen.DailyFragment
import com.example.sanay3yapp.ui.homeScreen.HomeFragment
import com.example.sanay3yapp.ui.jobScreen.JobFragment
import com.example.sanay3yapp.ui.offerScreen.ChatFragment
import com.example.sanay3yapp.ui.profileScreen.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (SessionUser.currentUserType == "dailyWorker") {
            val menu = mainBinding.bottomTabs.menu
            menu.removeItem(R.id.navigation_job)
            menu.removeItem(R.id.navigation_offer)
        }

        // Set up BottomNavigationView listener
        mainBinding.bottomTabs.setOnItemSelectedListener { item ->
            when (SessionUser.currentUserType) {
                "dailyWorker" -> showForDailyWorker(item)
                else -> showForWorkerClient(item)
            }
        }

        // Set default selected item if needed
        mainBinding.bottomTabs.selectedItemId = R.id.navigation_home
    }

    override fun onStart() {
        super.onStart()
        // Additional onStart logic if needed
    }

    private fun showFragmentSelected(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    fun changeFragmentTitle(title: String) {
        mainBinding.fragmentTitle.text = title
    }

    private fun showForWorkerClient(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_home -> showFragmentSelected(HomeFragment())
            R.id.navigation_job -> showFragmentSelected(JobFragment())
            R.id.navigation_profile -> showFragmentSelected(ProfileFragment())
            R.id.navigation_offer -> showFragmentSelected(ChatFragment())
            R.id.navigation_daily -> showFragmentSelected(DailyFragment())
            else -> false
        }
    }

    private fun showForDailyWorker(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.navigation_home -> showFragmentSelected(HomeFragment())
            R.id.navigation_profile -> showFragmentSelected(ProfileFragment())
            R.id.navigation_daily -> showFragmentSelected(DailyFragment())
            else -> false
        }
    }
}
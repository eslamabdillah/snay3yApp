package com.example.sanay3yapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityMainBinding
import com.example.sanay3yapp.ui.homeScreen.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Assuming you have a BottomNavigationView with id bottomTabs in your layout
        mainBinding.bottomTabs.setOnItemSelectedListener { item -> // Use setOnNavigationItemSelectedListener
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFragmentSelected(HomeFragment())
                    true // Return true to display the item as the selected item
                }

                R.id.navigation_job -> {
                    showFragmentSelected(HomeFragment())
                    true // Return true to display the item as the selected item
                }

                else -> false
            }
        }

        // Set default selected item if needed
        mainBinding.bottomTabs.selectedItemId = R.id.navigation_home
    }

    private fun showFragmentSelected(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
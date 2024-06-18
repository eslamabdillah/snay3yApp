package com.example.sanay3yapp.ui.homeScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentHomeBinding
import com.example.sanay3yapp.ui.Academy.Academy
import com.example.sanay3yapp.ui.AddJobDialogFragment
import com.example.sanay3yapp.ui.MainActivity

class HomeFragment : Fragment(), DialogDismissCallback {

    private var homeFragmentBinding: FragmentHomeBinding? = null
    private val binding get() = homeFragmentBinding!!
    lateinit var bottomSheetFragment: AddJobDialogFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("الرئيسية - الوظائف")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, HomeFragmentJobs())
            .commit()

        homeFragmentBinding!!.workers.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, HomeFragmentWorker())
                .commit()
        }

        homeFragmentBinding!!.offers.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, HomeFragmentJobs())
                .commit()
        }

        homeFragmentBinding!!.addJob.setOnClickListener({
            bottomSheetFragment = AddJobDialogFragment()
            bottomSheetFragment.setDialogDismissCallback(this)
            bottomSheetFragment.show(childFragmentManager, "addJobSheet")


        })

        homeFragmentBinding!!.Academy.setOnClickListener({
            val intent = Intent(context, Academy::class.java)
            startActivity(intent)
        })
    }

    override fun onDialogDismissed() {
        refreshFragment()
    }

    private fun refreshFragment() {

        Toast.makeText(context, "تم اضافة الوظيفة", Toast.LENGTH_SHORT).show()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, HomeFragmentJobs())
            .commit()
    }


}


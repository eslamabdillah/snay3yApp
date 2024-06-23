package com.example.sanay3yapp.ui.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.FragmentProfileWorkerBinding
import com.example.sanay3yapp.ui.MainActivity
import com.example.sanay3yapp.ui.SessionUser
import com.example.sanay3yapp.ui.authentication_forms.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileWorkerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileWorkerBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.changeFragmentTitle("حسابى")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (SessionUser.currentUserType) {
            "client" -> {
                setupProfileClient()


            }

            "worker" -> {
                setupProfileWorker()


            }

            "dailyWorker" -> {
                setupProfileDailyWorker()
            }

            else -> {


            }
        }
    }

    private fun setupProfileDailyWorker() {
        binding.name.text = SessionUser.dailyWorker.name
        binding.email.text = SessionUser.dailyWorker.email
        binding.city.text = SessionUser.dailyWorker.city
        binding.phone.text = SessionUser.dailyWorker.phone.toString()
        binding.idNational.isVisible = false
        binding.job.text = SessionUser.dailyWorker.job
        binding.exp.isVisible = false
        binding.clientsOpinions.visibility = View.GONE
        binding.ratingBar.visibility = View.GONE
        binding.txtExp.visibility = View.GONE
        binding.txtJob.visibility = View.VISIBLE
        binding.txtNational.visibility = View.GONE
        binding.workerGallery.isVisible = false
        Glide.with(this)
            .load(SessionUser.dailyWorker.photoUrl)
            .placeholder(R.drawable.logo) // Optional placeholder
            .error(R.drawable.logo)
            .into(binding.workerImage)
        binding.btnSignOut.setOnClickListener { signOut() }

    }

    private fun setupProfileClient() {
        binding.name.text = SessionUser.client.name
        binding.email.text = SessionUser.client.email
        binding.city.text = SessionUser.client.city
        binding.phone.text = SessionUser.client.phone.toString()
        binding.idNational.isVisible = false
        binding.job.isVisible = false
        binding.exp.isVisible = false
        binding.clientsOpinions.visibility = View.GONE
        binding.ratingBar.visibility = View.GONE
        binding.txtExp.visibility = View.GONE
        binding.txtJob.visibility = View.GONE
        binding.txtNational.visibility = View.GONE
        binding.clientsOpinions.setOnClickListener {
            loadChildFragment(FragmentClientsOpinions())
        }
        binding.workerGallery.isVisible = false
        binding.btnSignOut.setOnClickListener { signOut() }
    }

    private fun setupProfileWorker() {
        binding.name.text = SessionUser.worker.name
        binding.email.text = SessionUser.worker.email
        binding.city.text = SessionUser.worker.city
        binding.exp.text = SessionUser.worker.exp.toString()
        binding.job.text = SessionUser.worker.job
        binding.phone.text = SessionUser.worker.phone.toString()
        binding.idNational.text = SessionUser.worker.national_id.toString()

        Glide.with(this)
            .load(SessionUser.worker.photoUrl)
            .placeholder(R.drawable.logo) // Optional placeholder
            .error(R.drawable.logo)
            .into(binding.workerImage)


        binding.ratingBar.rating = SessionUser.worker.rate
        binding.clientsOpinions.setOnClickListener {
            val fragment = FragmentClientsOpinions.newInstance(SessionUser.worker.id)
            loadChildFragment(fragment)
        }
        binding.workerGallery.setOnClickListener {
            val fragment = FragmentGallery.newInstance(SessionUser.worker.id)
            loadChildFragment(fragment)
        }
        binding.btnSignOut.setOnClickListener { signOut() }


    }

    fun loadChildFragment(childFragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.profile_container, childFragment)
        transaction.commit()
    }

    private fun signOut() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        SessionUser.mode = false
        SessionUser.currentUserType = ""
        val context = requireContext()  // Ensure the context is non-null
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        activity?.finish()  // Optional: Finish the parent activity to remove it from the back stack
    }


}
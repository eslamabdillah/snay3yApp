package com.example.sanay3yapp.ui.Academy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.ActivityAcademyBinding

class Academy : AppCompatActivity() {

    lateinit var academyBinding: ActivityAcademyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        academyBinding = ActivityAcademyBinding.inflate(layoutInflater)
        setContentView(academyBinding.root)



// Setting click listeners for the views
          setJobTrainingClickListener(academyBinding.SBAKWorkers,"سباكة","سباك")
          setJobTrainingClickListener(academyBinding.KHRBAAYWorkers,"كهرباء","كهربائى")
          setJobTrainingClickListener(academyBinding.NAASHWorkers,"نقاشة","نقاش")
          setJobTrainingClickListener(academyBinding.BNAWorkers,"بنه","بنه")

    }

    fun setJobTrainingClickListener(view: View, workerType:String , job:String) {
        view.setOnClickListener {
            val intent = Intent(this, JobTraining::class.java).apply {
                putExtra("WORKER_TYPE", workerType)
                putExtra("WORKER_JOB", job)

            }
            startActivity(intent)
        }
    }
}
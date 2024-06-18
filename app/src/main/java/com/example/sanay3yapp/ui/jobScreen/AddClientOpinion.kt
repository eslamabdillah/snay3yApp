package com.example.sanay3yapp.ui.jobScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sanay3yapp.R
import com.example.sanay3yapp.databinding.AddClientOpinionBinding
import com.example.sanay3yapp.ui.MainActivity
import com.google.firebase.firestore.toObject
import dataBase.fireStore.DAO
import dataBase.models.ClientOpinion
import dataBase.models.Job

class AddClientOpinion : AppCompatActivity() {

    private lateinit var binding: AddClientOpinionBinding
    var ratingQuality = 0
    var ratingOnTime = 0
    var ratingWorkAgain = 0
    private var jobId: String? = null
    private var workerId: String? = null
    var nowJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddClientOpinionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve the jobId and workerId from the intent
        jobId = intent.getStringExtra("JOB_ID")
        workerId = intent.getStringExtra("WORKER_ID")

        Log.d("idjob", jobId ?: "not id")
        Log.d("idjobworker", workerId ?: "not id")



        setupRatingQualityBar()
        setupRatingOnTimeBar()
        setupRatingWorkAgainBar()

        binding.sendOpinion.setOnClickListener {
            Log.d("idjob", jobId ?: "not id")
            Log.d("idjobworker", workerId ?: "not id")
            DAO.getJob(jobId!!) { task ->
                if (task.isSuccessful) {
                    nowJob = task.result.toObject()
                    var opinion = ClientOpinion(
                        "",
                        nowJob!!.name,
                        ratingQuality.toFloat(),
                        ratingWorkAgain.toFloat(),
                        ratingOnTime.toFloat(),
                        binding.etClientOpinion.text.toString(),
                        nowJob!!.id,
                        nowJob!!.selectedWorker
                    )

                    DAO.addClientOpinion(
                        nowJob!!.selectedWorker,
                        opinion
                    ) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "add opinion", Toast.LENGTH_LONG).show()

                            val intentToMain = Intent(this, MainActivity::class.java)
                            startActivity(intentToMain)

                        } else {

                        }

                    }


                } else {

                }

            }


        }

    }

    private fun downloadJob(jobId: String?) {


    }

    private fun setupRatingQualityBar() {
        binding.ratingBarQuality.star1.setOnClickListener {
            ratingQuality = 1
            updateStarQualityRating(ratingQuality)
        }

        binding.ratingBarQuality.star2.setOnClickListener {
            ratingQuality = 2
            updateStarQualityRating(ratingQuality)
        }
        binding.ratingBarQuality.star3.setOnClickListener {
            ratingQuality = 3
            updateStarQualityRating(ratingQuality)
        }
        binding.ratingBarQuality.star4.setOnClickListener {
            ratingQuality = 4
            updateStarQualityRating(ratingQuality)
        }
        binding.ratingBarQuality.star5.setOnClickListener {
            ratingQuality = 5
            updateStarQualityRating(ratingQuality)
        }

    }

    private fun setupRatingOnTimeBar() {
        binding.ratingBarOnTime.star1.setOnClickListener {
            ratingOnTime = 1
            updateStarOnTimeRating(ratingOnTime)
        }

        binding.ratingBarOnTime.star2.setOnClickListener {
            ratingOnTime = 2
            updateStarOnTimeRating(ratingOnTime)
        }
        binding.ratingBarOnTime.star3.setOnClickListener {
            ratingOnTime = 3
            updateStarOnTimeRating(ratingOnTime)
        }
        binding.ratingBarOnTime.star4.setOnClickListener {
            ratingOnTime = 4
            updateStarOnTimeRating(ratingOnTime)
        }
        binding.ratingBarOnTime.star5.setOnClickListener {
            ratingOnTime = 5
            updateStarOnTimeRating(ratingOnTime)
        }

    }

    private fun setupRatingWorkAgainBar() {
        binding.ratingBarWorkAgain.star1.setOnClickListener {
            ratingWorkAgain = 1
            updateStarWorkAgainRating(ratingWorkAgain)
        }

        binding.ratingBarWorkAgain.star2.setOnClickListener {
            ratingWorkAgain = 2
            updateStarWorkAgainRating(ratingWorkAgain)
        }
        binding.ratingBarWorkAgain.star3.setOnClickListener {
            ratingWorkAgain = 3
            updateStarWorkAgainRating(ratingWorkAgain)
        }
        binding.ratingBarWorkAgain.star4.setOnClickListener {
            ratingWorkAgain = 4
            updateStarWorkAgainRating(ratingWorkAgain)
        }
        binding.ratingBarWorkAgain.star5.setOnClickListener {
            ratingWorkAgain = 5
            updateStarWorkAgainRating(ratingWorkAgain)
        }

    }


    private fun updateStarQualityRating(rating: Int) {
        val stars = arrayOf(
            binding.ratingBarQuality.star1,
            binding.ratingBarQuality.star2,
            binding.ratingBarQuality.star3,
            binding.ratingBarQuality.star4,
            binding.ratingBarQuality.star5,
        )

        stars.forEachIndexed { index, star ->
            if (index < rating) {
                star.setImageResource(R.drawable.ic_star_filled)
            } else {
                star.setImageResource(R.drawable.ic_star_border)
            }
        }
    }

    private fun updateStarOnTimeRating(rating: Int) {
        val stars = arrayOf(
            binding.ratingBarOnTime.star1,
            binding.ratingBarOnTime.star2,
            binding.ratingBarOnTime.star3,
            binding.ratingBarOnTime.star4,
            binding.ratingBarOnTime.star5,
        )

        stars.forEachIndexed { index, star ->
            if (index < rating) {
                star.setImageResource(R.drawable.ic_star_filled)
            } else {
                star.setImageResource(R.drawable.ic_star_border)
            }
        }
    }

    private fun updateStarWorkAgainRating(rating: Int) {
        val stars = arrayOf(
            binding.ratingBarWorkAgain.star1,
            binding.ratingBarWorkAgain.star2,
            binding.ratingBarWorkAgain.star3,
            binding.ratingBarWorkAgain.star4,
            binding.ratingBarWorkAgain.star5,
        )

        stars.forEachIndexed { index, star ->
            if (index < rating) {
                star.setImageResource(R.drawable.ic_star_filled)
            } else {
                star.setImageResource(R.drawable.ic_star_border)
            }
        }
    }


}


package com.example.sanay3yapp.ui.jobScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sanay3yapp.R
import com.example.sanay3yapp.ui.MainActivity

fun showAlertDialog(context: Context, jobId: String, workerId: String) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)

    val builder = AlertDialog.Builder(context)
    builder.setView(dialogView)  // Set the custom view to the dialog

    // Create and show the dialog
    val dialog: AlertDialog = builder.create()
    dialog.show()

    // Access the custom buttons
    val positiveButton: Button = dialogView.findViewById(R.id.dialog_positive_button)
    val negativeButton: Button = dialogView.findViewById(R.id.dialog_negative_button)

    // Set up button click listeners
    positiveButton.setOnClickListener {
        Toast.makeText(context, "التقييم clicked", Toast.LENGTH_SHORT).show()
        Log.d("jobIDinalert", jobId)
        val intent = Intent(context, AddClientOpinion::class.java)
        intent.putExtra("JOB_ID", jobId)
        intent.putExtra("WORKER_ID", workerId)
        context.startActivity(intent)
        dialog.dismiss()
    }

    negativeButton.setOnClickListener {
        Toast.makeText(context, "الغاء clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        dialog.dismiss()
    }
}
package com.example.sanay3yapp.ui.jobScreen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.sanay3yapp.R
import com.example.sanay3yapp.ui.MainActivity

fun showAlertDialogAfterComplaint(context: Context) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_send_complaint, null)

    val builder = AlertDialog.Builder(context)
    builder.setView(dialogView)  // Set the custom view to the dialog

    // Create and show the dialog
    val dialog: AlertDialog = builder.create()
    dialog.show()

    // Access the custom buttons
    val positiveButton: Button = dialogView.findViewById(R.id.dialog_positive_button)

    // Set up button click listeners
    positiveButton.setOnClickListener {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        dialog.dismiss()
    }


}
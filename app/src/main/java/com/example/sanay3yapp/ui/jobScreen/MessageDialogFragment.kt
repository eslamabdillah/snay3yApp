package com.example.sanay3yapp.ui.jobScreen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.sanay3yapp.R

class MessageDialogFragment : DialogFragment() {

    interface OnButtonClickListener {
        fun onButtonClick()
    }

    private var listener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Try to attach the listener from the parent fragment
            listener = parentFragment as? OnButtonClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$parentFragment must implement OnButtonClickListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.pop_fragment, null)
            builder.setView(view)

            view.findViewById<Button>(R.id.actionButton).setOnClickListener {
                listener?.onButtonClick()
                dismiss()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
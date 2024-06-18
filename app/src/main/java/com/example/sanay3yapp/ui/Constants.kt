package com.example.sanay3yapp.ui

import android.icu.text.SimpleDateFormat
import com.google.firebase.Timestamp
import java.util.Locale

object Constants {
    var idForSignUp: String = ""
    var workerType: String = ""
    var dialogVisible: Boolean = false

}

object StatesJob {
    var NEW: String = "new"
    var INWORK: String = "inwork"
    var FINISHED: String = "finshed"
}

object UserTypes {
    var CLIENT: Int = 1
    var WORKER: Int = 2
}

object Functions {
    fun convertToDate(timestamp: Timestamp): String {
        var date = timestamp.toDate()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date) // Format the date
    }

}



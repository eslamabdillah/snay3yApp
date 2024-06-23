package dataBase.models

import android.os.Parcelable
import com.example.sanay3yapp.ui.StatesJob
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    var id: String = "",
    var name: String = "",
    var details: String = "",
    var cost: Int = 0,
    var duration: Int = 0,
    var workerOffers: MutableList<Offer> = mutableListOf(),
    var owner: String = "",
    var ownerName: String = "",
    var date: Timestamp = Timestamp.now(),
    var state: String = StatesJob.NEW,
    var selectedOffer: String = "",
    var selectedWorker: String = "",
    var city: String = ""


) : Parcelable

package dataBase.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    var id: String = "",
    var name: String = "",
    var details: String = "",
    var cost: Int = 0,
    var duration: Int = 0,
    var workerOffers: MutableList<Offer> = mutableListOf()
) : Parcelable

package dataBase.models

import com.google.firebase.Timestamp

data class Job(
    var id: String = "",
    var name: String = "",
    var details: String = "",
    var cost: Int = 0,
    var duration: Int = 0,
    var workerOffers: MutableList<Offer> = mutableListOf(),
    var owner: String = "",
    var date: Timestamp = Timestamp.now()

)

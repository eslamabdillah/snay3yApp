package dataBase.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class Offer(
    var id: String = "",
    var details: String = "",
    var cost: Int = 0,
    var duration: Int = 0,
    var workerId: String = "",
    var jobId: String = ""

) : Parcelable

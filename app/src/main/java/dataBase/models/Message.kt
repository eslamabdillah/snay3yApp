package dataBase.models

import com.google.firebase.Timestamp

data class Message(
    var ownerType: String = "",
    var ownerId: String = "",
    var content: String = "",
    var time: Timestamp = Timestamp.now()

)

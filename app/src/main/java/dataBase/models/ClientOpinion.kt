package dataBase.models

data class ClientOpinion(
    var id: String = "",
    var jobName: String = "",
    var rateQuality: Float = 0f,
    var rateWorkAgain: Float = 0f,
    var rateDelivery: Float = 0f,
    var opinionText: String = "",
    var jobId: String = ""
)

package dataBase.models

data class Complaint(
    var id: String = "",
    var content: String = "",
    var jobId: String = "",
    var workerId: String = "",
    var ClientId: String = "",
    var from: String = ""
)

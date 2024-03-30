package dataBase.models

data class Client(
    var name: String = "",
    var age: Int = 0,
    var phone: Int = 0,
    var place: String = "",
    var myOffers: MutableList<Job>,
    var current_job: Job
)

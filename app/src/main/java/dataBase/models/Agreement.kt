package dataBase.models

data class Agreement(
    var job: Job = Job(),
    var offer: Offer = Offer(),
    var client: Client = Client(),
    var worker: Worker = Worker(),
)

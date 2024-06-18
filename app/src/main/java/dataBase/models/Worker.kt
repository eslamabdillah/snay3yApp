package dataBase.models


data class Worker(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var type: String = "worker",
    var age: Int = 0,
    var passWord: String = "",
    var phone: Long = 0,
    var national_id: Long = 0,
    var job: String = "",
    var city: String = "",
    var exp: Int = 0,
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var photoUrl: String = "",
    var myOffers: MutableList<Offer>? = null,
    var rate: Float = 0f,
    var completeJobsList: MutableList<String>? = null,
    var currentJob: String = "",
    var chatRooms: MutableList<String>? = null,


// TODO: my_Offers , current_Job , Clients_opinion

)
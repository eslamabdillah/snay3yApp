package dataBase.models


data class Worker(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var type: String = "worker",
    var age: Int = 0, // Default value needed
    var passWord: String = "", // Default value needed
    var phone: Long = 0, // Default value needed
    var national_id: Long = 0, // Default value needed
    var job: String = "", // Default value needed
    var city: String = "", // Default value needed
    var exp: Int = 0, // Default value needed
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0

// TODO: my_Offers , current_Job , Clients_opinion

)
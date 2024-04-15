package dataBase.models

data class DailyWorker(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var type: String = "assistant",
    var passWord: String = "", // Default value needed
    var phone: Long = 0, // Default value needed
    var job: String = "", // Default value needed
    var city: String = "", // Default value needed
    var photoUrl: String = "",
    var rate: Float = 0f,
    var daily_rent: Int = 0
)

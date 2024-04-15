package dataBase.models

data class Client(
    var id: String = "",
    var email: String = "",
    var passWord: String = "",
    var name: String = "",
    var age: Int = 0,
    var phone: Long = 0L,
    var city: String = "",
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var type: String = "client",
    var myJobs: MutableList<String>? = null

    )

package dataBase.models

data class GallaryProject(
    var workerId: String = "",
    var projectId: String = "",
    var nameProject: String = "",
    var detailsProject: String = "",
    var photoArray: MutableList<String> = mutableListOf(),


    )

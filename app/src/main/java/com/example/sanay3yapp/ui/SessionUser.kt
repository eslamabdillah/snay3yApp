package com.example.sanay3yapp.ui

import dataBase.models.Job
import dataBase.models.Offer

class SessionUser(
    var name: String = "",
    var id: String = "",
    var MyOffers: MutableList<Offer>,
    var currentJob: Job,
) {

}
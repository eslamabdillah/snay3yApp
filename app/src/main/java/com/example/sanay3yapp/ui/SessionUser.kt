package com.example.sanay3yapp.ui

import dataBase.models.Client
import dataBase.models.Worker

object SessionUser {
    var mode: Boolean = false
    var currentUserType = ""
    var worker = Worker()
    var client = Client()
}
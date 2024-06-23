package com.example.sanay3yapp.ui

import dataBase.models.Admin
import dataBase.models.Client
import dataBase.models.DailyWorker
import dataBase.models.Worker

object SessionUser {
    var mode: Boolean = false
    var currentUserType = ""
    var worker = Worker()
    var client = Client()
    var admin = Admin()
    var dailyWorker = DailyWorker()
}
package com.example.osnovi_recyclerview

import android.app.Application
import com.example.osnovi_recyclerview.model.UserService

class App: Application() {

    val usersService = UserService()

}
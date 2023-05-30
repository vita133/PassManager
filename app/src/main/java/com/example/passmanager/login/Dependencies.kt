package com.example.passmanager.login

import android.content.Context
import androidx.room.Room
import com.example.passmanager.login.model.database.LoginDB
import com.example.passmanager.login.model.UserRepository


object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val db: LoginDB by lazy {
        Room.databaseBuilder(applicationContext, LoginDB::class.java, "login.db")
            .createFromAsset("room_article.db")
            .build()
    }

    val UserRepository: UserRepository by lazy { UserRepository(db.getDao()) }
}

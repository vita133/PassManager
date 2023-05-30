package com.example.passmanager.login.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passmanager.login.model.entities.LoginEntity

@Database(entities = [LoginEntity::class], version = 1)
abstract class LoginDB : RoomDatabase() {
    abstract fun getDao(): LoginDao

    companion object {
        fun getLoginDB(context : Context) : LoginDB{
            return Room.databaseBuilder(
                context.applicationContext,
                LoginDB::class.java,
                "Login.db"
            ).build()
        }
    }
}
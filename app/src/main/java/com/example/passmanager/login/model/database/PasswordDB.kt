package com.example.passmanager.login.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passmanager.login.model.entities.PasswordEntity

@Database(entities = [PasswordEntity::class], version = 1)
abstract class PasswordDB : RoomDatabase() {
    abstract fun getDao(): PasswordDao

    companion object {
        fun getPasswordDB(context : Context) : PasswordDB{
            return Room.databaseBuilder(
                context.applicationContext,
                PasswordDB::class.java,
                "Passwords.db"
            ).build()
        }
    }
}
package com.example.passmanager.login.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.passmanager.login.model.entities.LoginEntity


@Dao
interface LoginDao {
    @Insert
    fun insert(item: LoginEntity)
    @Query("SELECT * FROM Users WHERE UserName = :username")
    fun getUserByUsername(username: String): LoginEntity?
    @Query("SELECT * FROM Users WHERE UserName = :username AND UserPasswordHash = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): LoginEntity?
}
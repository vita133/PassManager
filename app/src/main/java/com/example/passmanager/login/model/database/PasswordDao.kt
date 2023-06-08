package com.example.passmanager.login.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.passmanager.login.model.entities.PasswordEntity

@Dao
interface PasswordDao {
    @Insert
    fun insert(password: PasswordEntity)

    @Query("SELECT * FROM passwords WHERE UserName = :username")
    fun getAllPasswords(username: String): List<PasswordEntity>

    @Query("SELECT * FROM passwords WHERE UserName = :username AND WebSite = :sitename")
    fun getPasswordByName(username: String, sitename: String): PasswordEntity?

    @Query(" DELETE FROM passwords WHERE UserName = :username AND WebSite = :sitename")
    fun deletePasswordByName(username: String, sitename: String)
}

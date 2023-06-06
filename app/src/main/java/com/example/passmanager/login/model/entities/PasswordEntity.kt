package com.example.passmanager.login.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "UserName")
    val userName: String,
    @ColumnInfo(name = "WebSite")
    val website: String,
    @ColumnInfo(name = "Password")
    val password: String
)
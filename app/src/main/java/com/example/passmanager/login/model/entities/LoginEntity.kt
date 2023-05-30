package com.example.passmanager.login.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class LoginEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "UserName")
    var userName: String,
    @ColumnInfo(name = "UserPassword")
    var userPassword: String,
)
package com.example.passmanager.login.model

import com.example.passmanager.login.model.database.LoginDao
import com.example.passmanager.login.model.entities.LoginEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: LoginDao) {
    suspend fun insertUser(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val user = LoginEntity(null, username, password)
            userDao.insert(user)
        }
    }

    suspend fun getUserByUsername(username: String): LoginEntity? {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.getUserByUsername(username)
        }
    }

    suspend fun getUserByUsernameAndPassword(username: String, password: String): LoginEntity? {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.getUserByUsernameAndPassword(username, password)
        }
    }
}
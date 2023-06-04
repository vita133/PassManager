package com.example.passmanager.login.model

import com.example.passmanager.login.model.database.LoginDao
import com.example.passmanager.login.model.entities.LoginEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.passmanager.login.util.HashUtils

class UserRepository(private val userDao: LoginDao) {
    suspend fun insertUser(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val salt = HashUtils.generateSalt()
            val hashedPass = HashUtils.sha256Hash(password + salt)
            val hashedUsername = HashUtils.sha256Hash(username)
            val user = LoginEntity(null, hashedUsername, hashedPass, salt)
            userDao.insert(user)
        }
    }

    suspend fun getUserByUsername(username: String): LoginEntity? {
        return withContext(Dispatchers.IO) {
            val hashedUsername = HashUtils.sha256Hash(username)
            return@withContext userDao.getUserByUsername(hashedUsername)
        }
    }

    suspend fun getUserByUsernameAndPassword(username: String, password: String): LoginEntity? {
        return withContext(Dispatchers.IO) {
            val hashedUsername = HashUtils.sha256Hash(username)
            val user = userDao.getUserByUsername(hashedUsername)
            if (user != null) {
                val hashedPass = HashUtils.sha256Hash(password + user.userPasswordSalt)
                return@withContext userDao.getUserByUsernameAndPassword(hashedUsername, hashedPass)
            }
            return@withContext null
        }
    }
}
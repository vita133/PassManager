package com.example.passmanager.login.model

import com.example.passmanager.login.model.database.PasswordDao
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.HashUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository (private val passDao: PasswordDao) {

        suspend fun insertPassword(username: String, passname: String, password: String) {
            withContext(Dispatchers.IO) {
                val hashedUsername = HashUtils.sha256Hash(username)
                val passEntity = PasswordEntity(null, hashedUsername, passname, password)
                passDao.insert(passEntity)
                //шифрування ось тут
            }
        }

        suspend fun getPasswordByName(username: String, passname: String): PasswordEntity? {
            return withContext(Dispatchers.IO) {
                val hashedUsername = HashUtils.sha256Hash(username)
                return@withContext passDao.getPasswordByName(hashedUsername, passname)
            }
        }

        suspend fun getAllPasswords(username: String): List<PasswordEntity> {
            return withContext(Dispatchers.IO) {
                val hashedUsername = HashUtils.sha256Hash(username)
                return@withContext passDao.getAllPasswords(hashedUsername)
            }
        }
}
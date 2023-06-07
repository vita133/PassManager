package com.example.passmanager.login.model

import com.example.passmanager.login.model.database.PasswordDao
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.HashUtils
import com.example.passmanager.login.util.PasswordEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository (private val passDao: PasswordDao) {

    private val encryptor: PasswordEncryptor = PasswordEncryptor()

    suspend fun insertPassword(username: String, passname: String, password: String) {
        withContext(Dispatchers.IO) {
            val hashedUsername = HashUtils.sha256Hash(username)
            val encryptedPassword = encryptor.encryptPassword(password, username)
            val encryptedPassname = encryptor.encryptPassword(passname, username)
            val passEntity = PasswordEntity(null, hashedUsername, encryptedPassname, encryptedPassword)
            passDao.insert(passEntity)
        }
    }

    suspend fun getPasswordByName(username: String, passname: String): PasswordEntity? {
        return withContext(Dispatchers.IO) {
            val hashedUsername = HashUtils.sha256Hash(username)
            val encryptedPassname = encryptor.encryptPassword(passname, username)
            return@withContext passDao.getPasswordByName(hashedUsername, encryptedPassname)
        }
    }

    suspend fun getAllPasswords(username: String): List<PasswordEntity> {
        return withContext(Dispatchers.IO) {
            val hashedUsername = HashUtils.sha256Hash(username)
            return@withContext passDao.getAllPasswords(hashedUsername)
        }
    }
}
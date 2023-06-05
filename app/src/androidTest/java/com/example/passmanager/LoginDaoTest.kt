package com.example.passmanager

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.passmanager.login.model.database.LoginDB
import com.example.passmanager.login.model.entities.LoginEntity
import com.example.passmanager.login.model.database.LoginDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginDaoTest {

    private lateinit var database: LoginDB
    private lateinit var loginDao: LoginDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LoginDB::class.java
        ).build()
        loginDao = database.getDao()
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun insertAndGetUserByUsername() = runBlocking {
        val user = LoginEntity(userName = "john_doe", userPasswordHash = "password", userPasswordSalt = "")
        loginDao.insert(user)

        val fetchedUser = loginDao.getUserByUsername("john_doe")
        assert(fetchedUser?.userName == user.userName)
        assert(fetchedUser?.userPasswordHash == user.userPasswordHash)
    }

    @Test
    fun getUserByUsernameAndPassword() = runBlocking {
        val user = LoginEntity(userName = "john_doe", userPasswordHash = "password", userPasswordSalt = "")
        loginDao.insert(user)

        val fetchedUser = loginDao.getUserByUsernameAndPassword("john_doe", "password")
        assert(fetchedUser?.userName == user.userName)
        assert(fetchedUser?.userPasswordHash == user.userPasswordHash)
    }
}

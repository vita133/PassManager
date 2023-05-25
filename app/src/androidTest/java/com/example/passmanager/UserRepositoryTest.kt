package com.example.passmanager

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.passmanager.login.model.UserRepository
import com.example.passmanager.login.model.database.LoginDB
import com.example.passmanager.login.model.database.LoginDao
import com.example.passmanager.login.model.entities.LoginEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRepositoryTest {
    private lateinit var userDao: LoginDao
    private lateinit var userDatabase: LoginDB
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        userDatabase = Room.inMemoryDatabaseBuilder(context, LoginDB::class.java).build()
        userDao = userDatabase.getDao()
        userRepository = UserRepository(userDao)
    }

    @After
    fun cleanup() {
        userDatabase.close()
    }

    @Test
    fun testInsertUser() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        userRepository.insertUser(username, password)

        val user = userDao.getUserByUsername(username)
        assertEquals(username, user?.userName)
        assertEquals(password, user?.userPassword)
    }

    @Test
    fun testGetUserByUsername() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        val user = LoginEntity(null, username, password)
        userDao.insert(user)

        val retrievedUser = userRepository.getUserByUsername(username)
        assertEquals(username, retrievedUser?.userName)
        assertEquals(password, retrievedUser?.userPassword)
    }

    @Test
    fun testGetUserByUsernameAndPassword() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        val user = LoginEntity(null, username, password)
        userDao.insert(user)

        val retrievedUser = userRepository.getUserByUsernameAndPassword(username, password)
        assertEquals(username, retrievedUser?.userName)
        assertEquals(password, retrievedUser?.userPassword)
    }

    @Test
    fun testGetUserByUsernameAndPassword_IncorrectPassword() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        val user = LoginEntity(null, username, password)
        userDao.insert(user)

        val retrievedUser = userRepository.getUserByUsernameAndPassword(username, "incorrect")
        assertNull(retrievedUser)
    }
}

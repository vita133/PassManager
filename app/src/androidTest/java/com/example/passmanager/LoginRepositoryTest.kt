package com.example.passmanager

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.passmanager.login.model.LoginRepository
import com.example.passmanager.login.model.database.LoginDB
import com.example.passmanager.login.model.database.LoginDao
import com.example.passmanager.login.model.entities.LoginEntity
import com.example.passmanager.login.util.HashUtils
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginRepositoryTest {
    private lateinit var userDao: LoginDao
    private lateinit var userDatabase: LoginDB
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        userDatabase = Room.inMemoryDatabaseBuilder(context, LoginDB::class.java).build()
        userDao = userDatabase.getDao()
        loginRepository = LoginRepository(userDao)
    }

    @After
    fun cleanup() {
        userDatabase.close()
    }

    @Test
    fun testInsertUser() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        loginRepository.insertUser(username, password)

        val hashedUsername = HashUtils.sha256Hash(username)
        val user = userDao.getUserByUsername(hashedUsername)
        val hashedPass = HashUtils.sha256Hash(password + user?.userPasswordSalt)
        assertEquals(hashedUsername, user?.userName)
        assertEquals(hashedPass, user?.userPasswordHash)
    }
    
    @Test
    fun testInsertUser_ExistingUsername() = runBlocking {
        val username = "testuser"
        val password = "testpass"

        val hashedUsername = HashUtils.sha256Hash(username)
        val existingUser = LoginEntity(null, hashedUsername, "existingpass", "")
        userDao.insert(existingUser)
        loginRepository.insertUser(username, password)
        val user = userDao.getUserByUsername(hashedUsername)
        assertEquals("existingpass", user?.userPasswordHash)
    }

    @Test
    fun testGetUserByUsername() = runBlocking {
        val username = "testuser"
        val password = "testpass"

        val salt = HashUtils.generateSalt()
        val hashedPass = HashUtils.sha256Hash(password + salt)
        val hashedUsername = HashUtils.sha256Hash(username)
        val user = LoginEntity(null, hashedUsername, hashedPass, salt)
        userDao.insert(user)

        val retrievedUser = loginRepository.getUserByUsername(username)
        assertEquals(hashedUsername, retrievedUser?.userName)
        assertEquals(hashedPass, retrievedUser?.userPasswordHash)
    }

    @Test
    fun testGetUserByUsernameAndPassword() = runBlocking {
        val username = "testuser"
        val password = "testpass"

        val salt = HashUtils.generateSalt()
        val hashedPass = HashUtils.sha256Hash(password + salt)
        val hashedUsername = HashUtils.sha256Hash(username)
        val user = LoginEntity(null, hashedUsername, hashedPass, salt)
        userDao.insert(user)

        val retrievedUser = loginRepository.getUserByUsernameAndPassword(username, password)
        assertEquals(hashedUsername, retrievedUser?.userName)
        assertEquals(hashedPass, retrievedUser?.userPasswordHash)
    }

    @Test
    fun testGetUserByUsernameAndPassword_IncorrectPassword() = runBlocking {
        val username = "testuser"
        val password = "testpass"
        val salt = HashUtils.generateSalt()
        val hashedPass = HashUtils.sha256Hash(password + salt)
        val hashedUsername = HashUtils.sha256Hash(username)
        val user = LoginEntity(null, hashedUsername, hashedPass, salt)
        userDao.insert(user)

        val retrievedUser = loginRepository.getUserByUsernameAndPassword(username, "incorrect")
        assertNull(retrievedUser)
    }
}

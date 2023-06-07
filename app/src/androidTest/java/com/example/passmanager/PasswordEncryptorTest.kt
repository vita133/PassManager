package com.example.passmanager
import com.example.passmanager.login.util.PasswordEncryptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PasswordEncryptorTest {
    private lateinit var passwordEncryptor: PasswordEncryptor

    @Before
    fun setup() {
        val username = "myKey"
        passwordEncryptor = PasswordEncryptor(username)
    }

    @Test
    fun encryptPassword_returnsValidEncryption() {
        val password = "password123"

        val encryptedPassword = passwordEncryptor.encryptPassword(password)
        val decryptedPassword = passwordEncryptor.decryptPassword(encryptedPassword)

        assertEquals(password, decryptedPassword)
    }

    @Test
    fun decryptPassword_returnsValidDecryption() {
        val encryptedPassword = "svY77/rN6W5t0H4EuffjBoajuws2fFP70Q9y9ydY8X0="

        val decryptedPassword = passwordEncryptor.decryptPassword(encryptedPassword)
        val expectedDecryptedPassword = "password4"

        assertEquals(expectedDecryptedPassword, decryptedPassword)
    }
}

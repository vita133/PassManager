package com.example.passmanager
import com.example.passmanager.login.util.PasswordEncryptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PasswordEncryptorTest {
    private lateinit var passwordEncryptor: PasswordEncryptor

    @Before
    fun setup() {
        passwordEncryptor = PasswordEncryptor()
    }

    @Test
    fun encryptPassword_returnsValidEncryption() {
        val password = "password123"

        val encryptedPassword = passwordEncryptor.encryptPassword(password,"myKey")
        val decryptedPassword = passwordEncryptor.decryptPassword(encryptedPassword,"myKey")

        assertEquals(password, decryptedPassword)
    }

    @Test
    fun decryptPassword_returnsValidDecryption() {
        val encryptedPassword = "dFK4c183Yva9xMlAwAn0uQ=="

        val decryptedPassword = passwordEncryptor.decryptPassword(encryptedPassword,"myKey")
        val expectedDecryptedPassword = "password4"

        assertEquals(expectedDecryptedPassword, decryptedPassword)
    }
}

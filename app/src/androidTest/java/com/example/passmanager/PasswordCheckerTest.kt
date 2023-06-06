package com.example.passmanager

import PasswordChecker
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordCheckerTest {

    @Test
    fun testCompromisedPassword() {
        val password1 = "password"
        val password2 = "abc123"

        val passwordChecker = PasswordChecker()

        runBlocking {
            val compromised1 = passwordChecker.isPasswordCompromised(password1)
            assertTrue(compromised1)
            val compromised2 = passwordChecker.isPasswordCompromised(password2)
            assertTrue(compromised2)
        }
    }

    @Test
    fun testSecurePassword() {
        val password1 = "dKg2M18VzK7"
        val password2 = "secPas1w3@Ord"

        val passwordChecker = PasswordChecker()

        runBlocking {
            val compromised1 = passwordChecker.isPasswordCompromised(password1)
            assertFalse(compromised1)
            val compromised2 = passwordChecker.isPasswordCompromised(password2)
            assertFalse(compromised2)
        }
    }

    @Test
    fun testEmptyPassword() {
        val password = ""
        val passwordChecker = PasswordChecker()

        runBlocking {
            val compromised = passwordChecker.isPasswordCompromised(password)
            assertFalse(compromised)
        }
    }
}

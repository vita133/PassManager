package com.example.passmanager

import PasswordChecker
import com.example.passmanager.login.model.generation.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PasswordGeneratorTest {

    private lateinit var passwordChecker: PasswordChecker
    private lateinit var passwordGenerator: PasswordGenerator

    @Before
    fun setup() {
        passwordChecker = PasswordChecker()
        passwordGenerator = PasswordGenerator(8, passwordChecker)
    }

    @Test
    fun testGeneratePasswordLength() {
        passwordGenerator.add(Numbers())

        val generatedPassword = passwordGenerator.generatePassword().first
        assertEquals(8, generatedPassword.length)
    }

    @Test
    fun testGeneratePasswordCharacterSet() {
        passwordGenerator.add(UpperCaseLetters())
        passwordGenerator.add(LowerCaseLetters())
        passwordGenerator.add(Numbers())
        passwordGenerator.add(Symbols())

        val generatedPassword = passwordGenerator.generatePassword().first
        val expectedCharacterSet = UpperCaseLetters().getAllowedChars() +
                LowerCaseLetters().getAllowedChars() +
                Numbers().getAllowedChars() +
                Symbols().getAllowedChars()

        assertTrue(generatedPassword.all { expectedCharacterSet.contains(it) })
    }

    @Test
    fun testClearCharacterSets() {
        passwordGenerator.add(UpperCaseLetters())
        passwordGenerator.add(LowerCaseLetters())
        passwordGenerator.add(Numbers())
        passwordGenerator.add(Symbols())

        passwordGenerator.clear()

        val generatedPassword = passwordGenerator.generatePassword().first
        assertEquals(0, generatedPassword.length)
        assertEquals("", generatedPassword)
    }
}

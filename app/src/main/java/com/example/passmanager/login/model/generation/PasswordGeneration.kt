package com.example.passmanager.login.model.generation

import PasswordChecker
import kotlinx.coroutines.runBlocking

class PasswordGenerator(
    private val length: Int,
    private val passChecker: PasswordChecker
) {
    init {
        require(length > 0) { "Length should be more than 0" }
    }
    private val characterSetsList: MutableList<CharacterSet> = mutableListOf()

    fun add(characterSet: CharacterSet) {
        characterSetsList.add(characterSet)
    }

    fun clear() {
        characterSetsList.clear()
    }

    fun generatePassword(): Pair<String, Boolean> = runBlocking {
        val password: String = generateRandomPassword()
        println("Password generated: $password")
        val isCompromised: Boolean = passChecker.isPasswordCompromised(password)
        return@runBlocking Pair(password, isCompromised)
    }

    private fun generateRandomPassword(): String {
        val allowedChars = characterSetsList
            .flatMap { it.getAllowedChars() }

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}

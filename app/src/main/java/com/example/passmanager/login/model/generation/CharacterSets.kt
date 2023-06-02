package com.example.passmanager.login.model.generation

interface CharacterSet {
    fun getAllowedChars(): List<Char>
}

class Numbers : CharacterSet {
    override fun getAllowedChars(): List<Char> = ('0'..'9').toList()
}

class LowerCaseLetters : CharacterSet {
    override fun getAllowedChars(): List<Char> = ('a'..'z').toList()
}

class UpperCaseLetters : CharacterSet {
    override fun getAllowedChars(): List<Char> = ('A'..'Z').toList()
}

class Symbols : CharacterSet {
    override fun getAllowedChars(): List<Char> = listOf('!', '@', '#', '$', '%', '&', '*', '?')
}
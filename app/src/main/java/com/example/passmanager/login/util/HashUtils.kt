package com.example.passmanager.login.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

object HashUtils {
    fun sha256Hash(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(input.toByteArray())
            val hexString = StringBuilder()

            for (byte in hashBytes) {
                val hex = Integer.toHexString(0xFF and byte.toInt())
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }

            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    fun generateSalt(): String {
        val secureRandom = SecureRandom()
        val saltBytes = ByteArray(16)
        secureRandom.nextBytes(saltBytes)
        return bytesToHexString(saltBytes)
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val hexString = StringBuilder()
        for (byte in bytes) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }
}

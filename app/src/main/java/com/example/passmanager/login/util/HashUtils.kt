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


//
//import java.security.MessageDigest
//import java.security.SecureRandom
//
//object HashUtils {
//    private const val SALT_LENGTH = 16
//
//    fun hashString(value: String): String {
//        val salt = generateSalt()
//        val saltedValue = salt.plus(value.toByteArray())
//        val hashedBytes = hashBytes(saltedValue)
//        return bytesToHex(salt + hashedBytes)
//    }
//
//    private fun generateSalt(): ByteArray {
//        val random = SecureRandom()
//        val salt = ByteArray(SALT_LENGTH)
//        random.nextBytes(salt)
//        return salt
//    }
//
//    private fun hashBytes(data: ByteArray): ByteArray {
//        val md = MessageDigest.getInstance("SHA-256")
//        return md.digest(data)
//    }
//
//    private fun bytesToHex(bytes: ByteArray): String {
//        val hexChars = CharArray(bytes.size * 2)
//        for (i in bytes.indices) {
//            val v = bytes[i].toInt() and 0xFF
//            hexChars[i * 2] = hexArray[v ushr 4]
//            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
//        }
//        return String(hexChars)
//    }
//
//    private val hexArray = "0123456789ABCDEF".toCharArray()
//}


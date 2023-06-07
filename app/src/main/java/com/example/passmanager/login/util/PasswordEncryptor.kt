package com.example.passmanager.login.util

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class PasswordEncryptor() {
    private val cipherAlgorithm = "AES/CBC/PKCS7Padding"
    private val keySize = 256
    private val ivSize = 16
    private val fixedIV = byteArrayOf(
        0x4C, 0x68, 0x54, 0x4D, 0x48, 0x6D, 0x65, 0x79,
        0x43, 0x48, 0x61, 0x6E, 0x67, 0x65, 0x49, 0x56
    )

    private fun generateSecretKeySpec(encryptionKey: String): SecretKeySpec {
        val md = MessageDigest.getInstance("SHA-256")
        val encryptionKeyBytes = encryptionKey.toByteArray(Charsets.UTF_8)
        val digest = md.digest(encryptionKeyBytes)
        return SecretKeySpec(digest, "AES")
    }

    fun encryptPassword(password: String, encryptionKey: String): String {
        val cipher = Cipher.getInstance(cipherAlgorithm)
        val ivParameterSpec = IvParameterSpec(fixedIV)
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKeySpec(encryptionKey), ivParameterSpec)
        val encryptedBytes = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decryptPassword(encryptedPassword: String, encryptionKey: String): String {
        val encryptedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val ivParameterSpec = IvParameterSpec(fixedIV)
        val cipher = Cipher.getInstance(cipherAlgorithm)
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKeySpec(encryptionKey), ivParameterSpec)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}
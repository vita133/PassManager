package com.example.passmanager.login.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher


import java.security.MessageDigest
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec




class PasswordEncryptor(username: String) {
    private val cipherAlgorithm = "AES/CBC/PKCS7Padding"
    //private val keySize = 256
    private val ivSize = 16
    private val encryptionKey = HashUtils.sha256Hash(username)

    private fun generateSecretKeySpec(): SecretKeySpec {
        val md = MessageDigest.getInstance("SHA-256")
        val encryptionKeyBytes = encryptionKey.toByteArray(Charsets.UTF_8)
        val digest = md.digest(encryptionKeyBytes)
        return SecretKeySpec(digest, "AES")
    }

    private fun generateIV(): IvParameterSpec {
        val ivBytes = ByteArray(ivSize)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(ivBytes)
        return IvParameterSpec(ivBytes)
    }

    fun encryptPassword(password: String): String {
        val cipher = Cipher.getInstance(cipherAlgorithm)
        val iv = generateIV()
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKeySpec(), iv)
        val encryptedBytes = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
        val encryptedData = iv.iv + encryptedBytes
        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    fun decryptPassword(encryptedPassword: String): String {
        val encryptedData = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val ivBytes = encryptedData.copyOfRange(0, ivSize)
        val encryptedBytes = encryptedData.copyOfRange(ivSize, encryptedData.size)
        val iv = IvParameterSpec(ivBytes)
        val cipher = Cipher.getInstance(cipherAlgorithm)
        cipher.init(Cipher.DECRYPT_MODE, generateSecretKeySpec(), iv)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}


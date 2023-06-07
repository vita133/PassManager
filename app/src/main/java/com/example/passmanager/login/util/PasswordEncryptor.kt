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

//
//
//class PasswordEncryptor(username: String) {
//    private val cipherAlgorithm = "AES/CBC/PKCS7Padding"
//    //private val keySize = 256
//    private val ivSize = 16
//    private val encryptionKey = HashUtils.sha256Hash(username)
//    private fun generateSecretKeySpec(): SecretKeySpec {
//        val md = MessageDigest.getInstance("SHA-256")
//        val encryptionKeyBytes = encryptionKey.toByteArray(Charsets.UTF_8)
//        val digest = md.digest(encryptionKeyBytes)
//        return SecretKeySpec(digest, "AES")
//    }
//
//    private fun generateIV(): IvParameterSpec {
//        val ivBytes = ByteArray(ivSize)
//        val secureRandom = SecureRandom()
//        secureRandom.nextBytes(ivBytes)
//        return IvParameterSpec(ivBytes)
//    }
//
//    fun encryptPassword(password: String): String {
//        val cipher = Cipher.getInstance(cipherAlgorithm)
//        val iv = generateIV()
//        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKeySpec(), iv)
//        val encryptedBytes = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
//        val encryptedData = iv.iv + encryptedBytes
//        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
//    }
//
//    fun decryptPassword(encryptedPassword: String): String {
//        val encryptedData = Base64.decode(encryptedPassword, Base64.DEFAULT)
//        val ivBytes = encryptedData.copyOfRange(0, ivSize)
//        val encryptedBytes = encryptedData.copyOfRange(ivSize, encryptedData.size)
//        val iv = IvParameterSpec(ivBytes)
//        val cipher = Cipher.getInstance(cipherAlgorithm)
//        cipher.init(Cipher.DECRYPT_MODE, generateSecretKeySpec(), iv)
//        val decryptedBytes = cipher.doFinal(encryptedBytes)
//        return String(decryptedBytes, Charsets.UTF_8)
//    }
//}
//import android.content.Context
//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import java.security.KeyStore
//import javax.crypto.KeyGenerator
//import javax.crypto.spec.GCMParameterSpec
//
//class PasswordEncryptor(private val context: Context) {
//    private val ivSize = 16
//    private val keystoreAlias = "my_key_alias"
//    private val cipherAlgorithm = KeyProperties.KEY_ALGORITHM_AES + "/" +
//            KeyProperties.BLOCK_MODE_GCM + "/" +
//            KeyProperties.ENCRYPTION_PADDING_NONE
//    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
//
//    init {
//        keyStore.load(null)
//        if (!keyStore.containsAlias(keystoreAlias)) {
//            generateEncryptionKey()
//        }
////    }
//    private val ivSize = 16
//    private val keystoreAlias = "my_key_alias"
//    private val cipherAlgorithm = KeyProperties.KEY_ALGORITHM_AES + "/" +
//            KeyProperties.BLOCK_MODE_GCM + "/" +
//            KeyProperties.ENCRYPTION_PADDING_NONE
//    private lateinit var keyStore: KeyStore
//
//    init {
//        initKeyStore()
//        if (!keyStore.containsAlias(keystoreAlias)) {
//            generateEncryptionKey()
//        }
//    }
//
//    private fun initKeyStore() {
//        keyStore = KeyStore.getInstance("AndroidKeyStore")
//        keyStore.load(null)
//    }
//
////    private fun generateEncryptionKey() {
////        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
////        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
////            keystoreAlias,
////            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
////        )
////            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
////            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
////            .build()
////        keyGenerator.init(keyGenParameterSpec)
////        keyGenerator.generateKey()
////    }
//    private fun generateEncryptionKey() {
//        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
//        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
//            keystoreAlias,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//        )
//            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//            .build()
//        keyGenerator.init(keyGenParameterSpec)
//        keyGenerator.generateKey()
//    }
//
//
//    private fun getSecretKey(): SecretKeySpec {
//        val secretKeyEntry = keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry
//        return SecretKeySpec(secretKeyEntry.secretKey.encoded, "AES")
//    }
//
//    private fun generateIV(): GCMParameterSpec {
//        val ivBytes = ByteArray(ivSize)
//        val secureRandom = SecureRandom()
//        secureRandom.nextBytes(ivBytes)
//        return GCMParameterSpec(128, ivBytes)
//    }
//
//    fun encryptPassword(password: String): String {
//        val cipher = Cipher.getInstance(cipherAlgorithm)
//        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), generateIV())
//        val encryptedBytes = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
//        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
//    }
//
//    fun decryptPassword(encryptedPassword: String): String {
//        val cipher = Cipher.getInstance(cipherAlgorithm)
//        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), generateIV())
//        val encryptedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
//        val decryptedBytes = cipher.doFinal(encryptedBytes)
//        return String(decryptedBytes, Charsets.UTF_8)
//    }
//}

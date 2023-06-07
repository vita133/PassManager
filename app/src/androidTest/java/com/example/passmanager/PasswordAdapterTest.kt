package com.example.passmanager


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.PasswordAdapter
import com.example.passmanager.login.util.PasswordEncryptor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PasswordAdapterTest {

    @Test
    fun getItemCount_returnsCorrectItemCount() {
        val passwords = listOf(
            PasswordEntity(1, "username", "site1", "password1"),
            PasswordEntity(2, "username", "site2", "password2"),
            PasswordEntity(3, "username", "site3", "password3")
        )
        val adapter = PasswordAdapter(passwords, "myKey")

        val itemCount = adapter.itemCount

        assertEquals(passwords.size, itemCount)
    }

    @Test
    fun updatePasswords_updatesPasswordsListAndNotifiesDataSetChanged() {
        val encryptor = PasswordEncryptor()
        val passwords1 = listOf(
            PasswordEntity(1,"username", encryptor.encryptPassword("site1","myKey"), encryptor.encryptPassword("password1", "myKey")),
            PasswordEntity(2,"username", encryptor.encryptPassword("site2","myKey"), encryptor.encryptPassword("password2", "myKey")),
            PasswordEntity(3,"username", encryptor.encryptPassword("site3","myKey"), encryptor.encryptPassword("password3", "myKey")),
        )
        val passwords2en = listOf(
            PasswordEntity(4,"username", encryptor.encryptPassword("site4", "myKey"), encryptor.encryptPassword("password4", "myKey")),
            PasswordEntity(5,"username", encryptor.encryptPassword("site5", "myKey"), encryptor.encryptPassword("password5", "myKey")),
        )
        val passwords2de = listOf(
            PasswordEntity(4, "username", "site4", "password4"),
            PasswordEntity(5, "username", "site5", "password5")
        )
        val adapter = PasswordAdapter(passwords1, "myKey")

        adapter.updatePasswords(passwords2en)

        assertEquals(passwords2de, adapter.getPasswordsList())
        assertEquals(passwords2de.size, adapter.itemCount)
    }
}
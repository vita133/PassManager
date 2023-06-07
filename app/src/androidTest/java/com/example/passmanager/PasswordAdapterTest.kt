package com.example.passmanager


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.PasswordAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PasswordAdapterTest {

    @Test
    fun getItemCount_returnsCorrectItemCount() {
        var passwords = mutableListOf(
            PasswordEntity(1, "username", "site1", "password1"),
            PasswordEntity(2, "username", "site2", "password2"),
            PasswordEntity(3, "username", "site3", "password3")
        )
        val adapter = PasswordAdapter(passwords)

        val itemCount = adapter.itemCount

        assertEquals(passwords.size, itemCount)
    }

    @Test
    fun updatePasswords_updatesPasswordsListAndNotifiesDataSetChanged() {
        var passwords1 = mutableListOf(
            PasswordEntity(1, "username", "site1", "password1"),
            PasswordEntity(2, "username", "site2", "password2"),
            PasswordEntity(3, "username", "site3", "password3")
        )
        val passwords2 = mutableListOf(
            PasswordEntity(4, "username", "site4", "password4"),
            PasswordEntity(5, "username", "site5", "password5")
        )
        val adapter = PasswordAdapter(passwords1)

        adapter.updatePasswords(passwords2)

        assertEquals(passwords2, adapter.getPasswordsList())
        assertEquals(passwords2.size, adapter.itemCount)
    }
}

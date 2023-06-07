package com.example.passmanager.login.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R
import com.example.passmanager.login.model.entities.PasswordEntity



class PasswordAdapter(private val passwords: List<PasswordEntity>, private val hashKey: String) :
    RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {
    private val encryptor: PasswordEncryptor = PasswordEncryptor()
    private val passwordsList: MutableList<PasswordEntity> = passwords.toMutableList()
    fun getPasswordsList(): List<PasswordEntity> {
        return passwordsList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.passwords_row, parent, false)
        return PasswordViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return passwordsList.size
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val password = passwordsList[position]
        holder.bind(password)
    }
    private fun decryptPassword(encryptedPassword: String): String {
        return encryptor.decryptPassword(encryptedPassword, hashKey)
    }
    fun updatePasswords(newPasswords: List<PasswordEntity>) {
        passwordsList.clear()
        for (password in newPasswords) {
            val decryptedPassName = decryptPassword(password.website)
            val decryptedPassword = decryptPassword(password.password)
            val decryptedEntity = password.copy(website = decryptedPassName, password = decryptedPassword)
            passwordsList.add(decryptedEntity)
        }
        notifyDataSetChanged()
    }

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(password: PasswordEntity) {
            itemView.findViewById<TextView>(R.id.textView_passname).text = password.website
            itemView.findViewById<TextView>(R.id.textView_password).text = password.password
        }
    }
}
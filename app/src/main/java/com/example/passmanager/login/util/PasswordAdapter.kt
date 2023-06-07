package com.example.passmanager.login.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R
import com.example.passmanager.login.model.entities.PasswordEntity


class PasswordAdapter(private val passwords: MutableList<PasswordEntity>, private val hashKey: String) :
    RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {
    private val encryptor: PasswordEncryptor = PasswordEncryptor()
    private val passwordsList: MutableList<PasswordEntity> = mutableListOf()
    //private val passwordsList: MutableList<PasswordEntity> = passwords.toMutableList()
    init {
        passwordsList.addAll(passwords)
    }
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

    fun removePassword(position: Int) {
        passwordsList.removeAt(position)
        notifyItemRemoved(position)
        // код для видалення з бази даних
    }

    fun showDeleteConfirmationDialog(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null)
        val yesButton = dialogView.findViewById<Button>(R.id.button_yes)
        val noButton = dialogView.findViewById<Button>(R.id.button_no)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        yesButton.setOnClickListener {
            removePassword(position)
            alertDialog.dismiss()
        }

        noButton.setOnClickListener {
            notifyItemChanged(position)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(password: PasswordEntity) {
            itemView.findViewById<TextView>(R.id.textView_passname).text = password.website
            itemView.findViewById<TextView>(R.id.textView_password).text = password.password
        }
    }
}
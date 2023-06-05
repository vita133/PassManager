package com.example.passmanager.login.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R
import com.example.passmanager.login.model.entities.PasswordEntity


class PasswordAdapter(private val passwords: List<PasswordEntity>) :
    RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {

    private val passwordsList: MutableList<PasswordEntity> = passwords.toMutableList()

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

    fun updatePasswords(newPasswords: List<PasswordEntity>) {
        passwordsList.clear()
        passwordsList.addAll(newPasswords)
        notifyDataSetChanged()
    }

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(password: PasswordEntity) {
            itemView.findViewById<TextView>(R.id.textView_passname).text = password.website
            itemView.findViewById<TextView>(R.id.textView_password).text = password.password
        }
    }
}
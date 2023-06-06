package com.example.passmanager.login.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.PasswordViewModel
import com.example.passmanager.login.util.PasswordAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addPass: FloatingActionButton
    private lateinit var passwordViewModel: PasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        addPass = findViewById(R.id.flActionButton_addPasswd)

        val name = intent.getStringExtra("name").toString()

        val recyclerView: RecyclerView = findViewById(R.id.passwdRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = PasswordAdapter(emptyList())
        recyclerView.adapter = adapter


        passwordViewModel = ViewModelProvider(this)[PasswordViewModel::class.java]
        passwordViewModel.getAllPasswords(name)
        passwordViewModel.allPasswordsResult.observe(this@MainActivity) { passwords ->
            adapter.updatePasswords(passwords)
        }



        addPass.setOnClickListener {
            val intent = Intent(applicationContext, GenerationActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}
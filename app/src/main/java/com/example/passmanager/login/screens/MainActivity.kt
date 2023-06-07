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
import android.widget.Toast
import com.example.passmanager.login.util.SessionManagerUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var addPass: FloatingActionButton
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        addPass = findViewById(R.id.flActionButton_addPasswd)
        toolbar = findViewById(R.id.toolbar_main)

        val name = intent.getStringExtra("name").toString()

        val recyclerView: RecyclerView = findViewById(R.id.passwdRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PasswordAdapter(emptyList(), name)
        recyclerView.adapter = adapter


        passwordViewModel = ViewModelProvider(this)[PasswordViewModel::class.java]
        passwordViewModel.getAllPasswords(name)
        passwordViewModel.allPasswordsResult.observe(this@MainActivity) { passwords ->
            adapter.updatePasswords(passwords)
        }



        addPass.setOnClickListener {
            val isSessionActive = SessionManagerUtil.isSessionActive(Date(), applicationContext)
            if (isSessionActive) {
                val intent = Intent(applicationContext, GenerationActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
            } else {
                SessionManagerUtil.endUserSession(applicationContext)
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        toolbar.setNavigationOnClickListener{
            SessionManagerUtil.endUserSession(applicationContext)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val isSessionActive = SessionManagerUtil.isSessionActive(Date(), applicationContext)
        if (isSessionActive) {
            Toast.makeText(this, "Back button disabled", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}
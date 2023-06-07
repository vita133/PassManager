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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.SessionManagerUtil
import com.example.passmanager.login.util.SwipeToDeleteCallback
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
        val adapter = PasswordAdapter(mutableListOf<PasswordEntity>())
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finishAffinity()
        }

    }
}
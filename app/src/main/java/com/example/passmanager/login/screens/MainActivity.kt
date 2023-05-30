package com.example.passmanager.login.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passmanager.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addPass: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addPass = findViewById(R.id.flActionButton_addPasswd)

        addPass.setOnClickListener {
            val intent = Intent(applicationContext, GenerationActivity::class.java)
            startActivity(intent)
        }
    }
}
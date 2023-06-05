package com.example.passmanager.login.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.PasswordViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addPass: FloatingActionButton
    private lateinit var passwordViewModel: PasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        passwordViewModel = ViewModelProvider(this)[PasswordViewModel::class.java]
        addPass = findViewById(R.id.flActionButton_addPasswd)

        val name = intent.getStringExtra("name")




        addPass.setOnClickListener {
            val intent = Intent(applicationContext, GenerationActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}

///мають братись всі паролі з бази даних і відображатись
// тіпа всі гетолпасвордс
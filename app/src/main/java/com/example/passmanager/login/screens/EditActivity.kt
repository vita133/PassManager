package com.example.passmanager.login.screens

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.EditViewModel


class EditActivity : AppCompatActivity() {
    private lateinit var editViewModel: EditViewModel
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        editViewModel = ViewModelProvider(this)[EditViewModel::class.java]
        passwordEditText = findViewById(R.id.editText_password)


        val text = intent.getStringExtra("text")
        passwordEditText.setText(text)
    }
}

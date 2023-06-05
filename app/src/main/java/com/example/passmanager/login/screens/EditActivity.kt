package com.example.passmanager.login.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.PasswordViewModel


class EditActivity : AppCompatActivity() {
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var passwordEditText: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button
    private lateinit var passwordName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        passwordViewModel = ViewModelProvider(this)[PasswordViewModel::class.java]
        passwordEditText = findViewById(R.id.editText_password)
        buttonSave = findViewById(R.id.button_save)
        buttonDelete = findViewById(R.id.button_delete)
        passwordName = findViewById(R.id.editText_passName)

        val text = intent.getStringExtra("text")
        passwordEditText.setText(text)
        val username = intent.getStringExtra("name").toString()

        buttonSave.setOnClickListener {
            val passName = passwordName.text.toString()
            val pass = passwordEditText.text.toString()

            if(passName.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this@EditActivity, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            } else {
                passwordViewModel.getPasswordByName(username, passName)
                passwordViewModel.passwordByNameResult.observe(this@EditActivity){existPass ->
                    if(existPass == null) {
                        passwordViewModel.insertPassword(username, passName, pass)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("name", username)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@EditActivity, "You already have password with this name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

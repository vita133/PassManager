package com.example.passmanager.login.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.PasswordViewModel
import com.example.passmanager.login.util.SessionManagerUtil
import java.util.*


class EditActivity : AppCompatActivity() {
    private lateinit var passwordViewModel: PasswordViewModel
    private lateinit var passwordEditText: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button
    private lateinit var passwordName: EditText
    private lateinit var back: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val username = intent.getStringExtra("name").toString()
        val textPassword = intent.getStringExtra("textPassword")

        passwordViewModel = ViewModelProvider(this)[PasswordViewModel::class.java]
        passwordEditText = findViewById(R.id.editText_password)
        buttonSave = findViewById(R.id.button_save)
        buttonDelete = findViewById(R.id.button_delete)
        passwordName = findViewById(R.id.editText_passName)
        back = findViewById(R.id.textView_back)

        passwordEditText.setText(textPassword)

        setupSaveButton(username)
        setupDeleteButton()
        setupBackButton(username)
    }

    private fun setupSaveButton(username:String ) {
        buttonSave.setOnClickListener {
            val isSessionActive = SessionManagerUtil.isSessionActive(Date(), applicationContext)
            if (isSessionActive) {
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
                            passwordViewModel.setNull()
                        }
                        passwordViewModel.passwordByNameResult.removeObservers(this@EditActivity)
                        passwordName.setText("")
                    }
                }
            } else {
                SessionManagerUtil.endUserSession(applicationContext)
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupDeleteButton(){
        val isSessionActive = SessionManagerUtil.isSessionActive(Date(), applicationContext)
        if (!isSessionActive) {
            SessionManagerUtil.endUserSession(applicationContext)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupBackButton(name : String) {
        back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}

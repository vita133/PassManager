package com.example.passmanager.login.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.passmanager.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnlogin: Button
    private lateinit var btnsignup: Button
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        username = findViewById(R.id.editTextLoginName)
        password = findViewById(R.id.editTextLoginPass)
        btnlogin = findViewById(R.id.buttonLogin)
        btnsignup = findViewById(R.id.buttonSignup)

        btnlogin.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    userViewModel.getUserByUsernameAndPassword(user, pass)
                }
                userViewModel.userByUsernameAndPasswordResult.observe(this@LoginActivity) { loggedInUser ->
                    if (loggedInUser != null) {
                        Toast.makeText(this@LoginActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        btnsignup.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}

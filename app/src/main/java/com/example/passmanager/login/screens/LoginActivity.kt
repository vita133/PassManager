package com.example.passmanager.login.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.passmanager.R
import com.example.passmanager.login.screens.VM.LoginViewModel
import com.example.passmanager.login.util.SessionManagerUtil
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnlogin: Button
    private lateinit var btnsignup: Button
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
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
                loginViewModel.getUserByUsernameAndPassword(user, pass)
                loginViewModel.userByUsernameAndPasswordResult.observe(this@LoginActivity) { loggedInUser ->
                    if (loggedInUser != null) {
                        val sessionToken = UUID.randomUUID().toString()
                        SessionManagerUtil.storeUserToken(applicationContext, sessionToken)
                        val expiresIn = 3600
                        SessionManagerUtil.startUserSession(applicationContext, expiresIn)
                        Toast.makeText(this@LoginActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        intent.putExtra("name", user)
                        startActivity(intent)
                        finishAffinity()
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

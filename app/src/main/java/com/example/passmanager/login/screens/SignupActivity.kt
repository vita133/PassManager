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
import com.example.passmanager.login.screens.VM.LoginViewModel
import com.example.passmanager.login.util.SessionManagerUtil
import java.util.*

class SignupActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repassword: EditText
    private lateinit var signup: Button
    private lateinit var back: TextView
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.editTextSignupName)
        password = findViewById(R.id.editTextSignupPass)
        repassword = findViewById(R.id.editTextSignupConfPass)
        signup = findViewById(R.id.buttonSignup2)
        back = findViewById(R.id.textView_backsignup)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        signup.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val repass = repassword.text.toString()

            if (user.isBlank() || pass.isBlank() || repass.isBlank() || user.contains(" ") || pass.contains(" ") || repass.contains(" ")) {
                Toast.makeText(this@SignupActivity, "The field cannot be empty or contain spaces", Toast.LENGTH_SHORT).show()
            } else {
                if (pass == repass) {
                    loginViewModel.getUserByUsername(user)
                    loginViewModel.userByUsernameResult.observe(this@SignupActivity) { loggedInUser ->
                        if (loggedInUser == null) {
                            performUserSignup(user, pass)
                        } else {
                            Toast.makeText(
                                this@SignupActivity,
                                "User already exists! Please sign in",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        "Passwords not matching",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        back.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performUserSignup(user: String, pass: String) {
        val sessionToken = UUID.randomUUID().toString()
        SessionManagerUtil.storeUserToken(applicationContext, sessionToken)
        val expiresIn = 3600
        SessionManagerUtil.startUserSession(applicationContext, expiresIn)
        loginViewModel.insertUser(user, pass)
        Toast.makeText(
            this@SignupActivity,
            "Registered successfully",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        intent.putExtra("name", user)
        startActivity(intent)
    }
}

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

class SignupActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repassword: EditText
    private lateinit var signup: Button
    private lateinit var back: TextView
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.editTextSignupName)
        password = findViewById(R.id.editTextSignupPass)
        repassword = findViewById(R.id.editTextSignupConfPass)
        signup = findViewById(R.id.buttonSignup2)
        back = findViewById(R.id.textView_backsignup)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        signup.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val repass = repassword.text.toString()

            if (user.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(this@SignupActivity, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            } else {
                if (pass == repass) {
                    userViewModel.getUserByUsername(user)
                    userViewModel.userByUsernameResult.observe(this@SignupActivity) { loggedInUser ->
                        if (loggedInUser == null) {
                            userViewModel.insertUser(user, pass)
                            Toast.makeText(
                                this@SignupActivity,
                                "Registered successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
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
}

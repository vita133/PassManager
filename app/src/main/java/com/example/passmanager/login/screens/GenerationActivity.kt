package com.example.passmanager.login.screens

import PasswordChecker
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.passmanager.R
import com.example.passmanager.login.model.generation.*
import kotlinx.coroutines.*


class GenerationActivity : AppCompatActivity() {

    private lateinit var back: TextView
    private lateinit var seekbarLength: SeekBar
    private lateinit var edittextLength: EditText
    private lateinit var checkboxNumbers: CheckBox
    private lateinit var checkboxLowerCaseLetters: CheckBox
    private lateinit var checkboxUpperCaseLetters: CheckBox
    private lateinit var checkboxSymbols: CheckBox
    private lateinit var buttonGenerate: Button
    private lateinit var edittextPassword: EditText
    private lateinit var textviewCompromised: TextView

    private var passwordLength = 4
    private val colorGreen by lazy { ContextCompat.getColor(this, R.color.green) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation)

        back = findViewById(R.id.textView_back)
        seekbarLength = findViewById(R.id.seekBar_length)
        edittextLength = findViewById(R.id.editText_length)
        checkboxNumbers = findViewById(R.id.checkBox_numbers)
        checkboxLowerCaseLetters = findViewById(R.id.checkBox_lowercase)
        checkboxUpperCaseLetters = findViewById(R.id.checkBox_uppercase)
        checkboxSymbols = findViewById(R.id.checkBox_symbols)
        buttonGenerate = findViewById(R.id.button_generate)
        edittextPassword = findViewById(R.id.editText_genPassword)
        textviewCompromised = findViewById(R.id.textView_compromised)

        setupSeekBar()
        setupGenerateButton()
        setupBackButton()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupGenerateButton() {
        buttonGenerate.setOnClickListener {
            val passwordChecker = PasswordChecker()
            val passwordGenerator = PasswordGenerator(passwordLength, passwordChecker)

            passwordGenerator.clear()

            if (!checkboxLowerCaseLetters.isChecked && !checkboxUpperCaseLetters.isChecked && !checkboxSymbols.isChecked) {
                checkboxNumbers.isChecked = true
            }

            if (checkboxNumbers.isChecked){passwordGenerator.add(Numbers())}
            if ( checkboxLowerCaseLetters.isChecked){passwordGenerator.add(UpperCaseLetters())}
            if (checkboxUpperCaseLetters.isChecked){passwordGenerator.add(LowerCaseLetters())}
            if (checkboxSymbols.isChecked){passwordGenerator.add(Symbols())}

            GlobalScope.launch {
                val (passwordRes: String, isCompromised: Boolean) = passwordGenerator.generatePassword()

                withContext(Dispatchers.Main) {
                    println("Згенерований пароль: $passwordRes")

                    edittextPassword.setText(passwordRes)
                    textviewCompromised.visibility = View.VISIBLE

                    if (!isCompromised) {
                        println("Password is secure")
                        textviewCompromised.text = "Password is secure"
                        textviewCompromised.setTextColor(colorGreen)
                    } else {
                        println("Password is insecure")
                        textviewCompromised.text = "Password is insecure"
                        textviewCompromised.setTextColor(Color.RED)
                    }
                }
            }
        }
    }
    private fun setupSeekBar() {
        seekbarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                passwordLength = progress
                edittextLength.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupBackButton() {
        back.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
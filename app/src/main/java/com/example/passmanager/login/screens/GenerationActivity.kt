package com.example.passmanager.login.screens

import PasswordChecker
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
    private lateinit var seekBarLength: SeekBar
    private lateinit var editTextLength: EditText
    private lateinit var checkBoxNumbers: CheckBox
    private lateinit var checkBoxLowerCaseLetters: CheckBox
    private lateinit var checkBoxUpperCaseLetters: CheckBox
    private lateinit var checkBoxSymbols: CheckBox
    private lateinit var buttonGenerate: Button
    private lateinit var buttonSave: Button
    private lateinit var editTextPassword: EditText
    private lateinit var textViewCompromised: TextView
    private lateinit var copyButton: ImageView

    private var passwordLength = 4
    private val colorGreen by lazy { ContextCompat.getColor(this, R.color.green) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generation)

        back = findViewById(R.id.textView_back)
        seekBarLength = findViewById(R.id.seekBar_length)
        editTextLength = findViewById(R.id.editText_length)
        checkBoxNumbers = findViewById(R.id.checkBox_numbers)
        checkBoxLowerCaseLetters = findViewById(R.id.checkBox_lowercase)
        checkBoxUpperCaseLetters = findViewById(R.id.checkBox_uppercase)
        checkBoxSymbols = findViewById(R.id.checkBox_symbols)
        buttonGenerate = findViewById(R.id.button_generate)
        buttonSave = findViewById(R.id.button_save)
        editTextPassword = findViewById(R.id.editText_genPassword)
        textViewCompromised = findViewById(R.id.textView_compromised)
        copyButton = findViewById(R.id.imageView)

        val name = intent.getStringExtra("name")

        setupSeekBar()
        setupGenerateButton()
        setupBackButton()
        setupCopyButton()
        setupSaveButton(name)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupGenerateButton() {
        buttonGenerate.setOnClickListener {
            val passwordChecker = PasswordChecker()
            val passwordGenerator = PasswordGenerator(passwordLength, passwordChecker)

            passwordGenerator.clear()

            if (!checkBoxLowerCaseLetters.isChecked && !checkBoxUpperCaseLetters.isChecked && !checkBoxSymbols.isChecked) {
                checkBoxNumbers.isChecked = true
            }

            if (checkBoxNumbers.isChecked){passwordGenerator.add(Numbers())}
            if (checkBoxLowerCaseLetters.isChecked){passwordGenerator.add(UpperCaseLetters())}
            if (checkBoxUpperCaseLetters.isChecked){passwordGenerator.add(LowerCaseLetters())}
            if (checkBoxSymbols.isChecked){passwordGenerator.add(Symbols())}

            GlobalScope.launch {
                val (passwordRes: String, isCompromised: Boolean) = passwordGenerator.generatePassword()
                withContext(Dispatchers.Main) {
                    println("Generated password: $passwordRes")

                    editTextPassword.setText(passwordRes)
                    textViewCompromised.visibility = View.VISIBLE

                    if (!isCompromised) {
                        println("Password is secure")
                        textViewCompromised.text = "Password is secure"
                        textViewCompromised.setTextColor(colorGreen)
                    } else {
                        println("Password is insecure")
                        textViewCompromised.text = "Password is insecure"
                        textViewCompromised.setTextColor(Color.RED)
                    }
                }
            }
        }
    }

    private fun setupSeekBar() {
        seekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                passwordLength = progress
                editTextLength.setText(progress.toString())
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
    private fun setupSaveButton(name:String? ) {
        buttonSave.setOnClickListener {
            val text = editTextPassword.text.toString()
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("textPassword", text)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun setupCopyButton(){
        copyButton.setOnClickListener {
            val textToCopy = editTextPassword.text.toString()
            if (textToCopy.isNotEmpty()) {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Password", textToCopy)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(this, "Password copied to clipboard $textToCopy", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
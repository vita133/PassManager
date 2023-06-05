package com.example.passmanager.login.screens.VM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EditViewModel (application: Application) : AndroidViewModel(application) {
    private val editedPassword = MutableLiveData<String>()

    fun setEditedPassword(password: String) {
        editedPassword.value = password
    }

    fun getEditedPassword(): LiveData<String>{
        return editedPassword
    }
}
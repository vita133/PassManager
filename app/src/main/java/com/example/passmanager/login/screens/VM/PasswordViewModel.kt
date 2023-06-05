package com.example.passmanager.login.screens.VM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passmanager.login.model.PasswordRepository
import com.example.passmanager.login.model.database.PasswordDB
import com.example.passmanager.login.model.entities.PasswordEntity
import kotlinx.coroutines.launch

class PasswordViewModel (application: Application) : AndroidViewModel(application){
    private val passRepository: PasswordRepository
    private val db = PasswordDB.getPasswordDB(application)

    private val _passwordByNameResult = MutableLiveData<PasswordEntity?>()
    val passwordByNameResult: LiveData<PasswordEntity?> = _passwordByNameResult

    private val _allPasswordsResult = MutableLiveData<List<PasswordEntity>>()
    val allPasswordsResult: LiveData<List<PasswordEntity>> = _allPasswordsResult

    init {
        val passDao = db.getDao()
        passRepository = PasswordRepository(passDao)
    }

    fun insertPassword(username: String, passname:String, password: String) {
        viewModelScope.launch {
            passRepository.insertPassword(username, passname, password)
        }
    }

    fun getPasswordByName(username: String, passname: String) {
        viewModelScope.launch {
            val result = passRepository.getPasswordByName(username, passname)
            _passwordByNameResult.postValue(result)
        }
    }

    fun getAllPasswords(username: String) {
        viewModelScope.launch {
            val result = passRepository.getAllPasswords(username)
            _allPasswordsResult.postValue(result)
        }
    }
}
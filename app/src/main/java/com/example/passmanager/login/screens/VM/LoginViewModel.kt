package com.example.passmanager.login.screens.VM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passmanager.login.model.entities.LoginEntity
import com.example.passmanager.login.model.LoginRepository
import com.example.passmanager.login.model.database.LoginDB
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginRepository: LoginRepository
    private val db = LoginDB.getLoginDB(application)

    private val _userByUsernameResult = MutableLiveData<LoginEntity?>()
    val userByUsernameResult: LiveData<LoginEntity?> = _userByUsernameResult

    private val _userByUsernameAndPasswordResult = MutableLiveData<LoginEntity?>()
    val userByUsernameAndPasswordResult: LiveData<LoginEntity?> = _userByUsernameAndPasswordResult

    init {
        val userDao = db.getDao()
        loginRepository = LoginRepository(userDao)
    }

    fun insertUser(username: String, password: String) {
        viewModelScope.launch {
            loginRepository.insertUser(username, password)
        }

    }

    fun getUserByUsername(username: String) {
        viewModelScope.launch {
            val result = loginRepository.getUserByUsername(username)
            _userByUsernameResult.postValue(result)
        }
    }

    fun getUserByUsernameAndPassword(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.getUserByUsernameAndPassword(username, password)
            _userByUsernameAndPasswordResult.postValue(result)
        }
    }
    fun setNull() {
        viewModelScope.launch {
            _userByUsernameResult.postValue(null)
        }
    }
}

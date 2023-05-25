package com.example.passmanager.login.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.passmanager.login.model.entities.LoginEntity
import com.example.passmanager.login.model.UserRepository
import com.example.passmanager.login.model.database.LoginDB
import kotlinx.coroutines.launch


class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val db = LoginDB.getLoginDB(application)

    private val _userByUsernameResult = MutableLiveData<LoginEntity?>()
    val userByUsernameResult: LiveData<LoginEntity?> = _userByUsernameResult

    private val _userByUsernameAndPasswordResult = MutableLiveData<LoginEntity?>()
    val userByUsernameAndPasswordResult: LiveData<LoginEntity?> = _userByUsernameAndPasswordResult

    init {
        val userDao = db.getDao()
        userRepository = UserRepository(userDao)
    }

    fun insertUser(username: String, password: String) {
        viewModelScope.launch {
            userRepository.insertUser(username, password)
        }

    }

    fun getUserByUsername(username: String) {
        viewModelScope.launch {
            val result = userRepository.getUserByUsername(username)
            _userByUsernameResult.postValue(result)
        }
    }

    fun getUserByUsernameAndPassword(username: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.getUserByUsernameAndPassword(username, password)
            _userByUsernameAndPasswordResult.postValue(result)
        }
    }
}

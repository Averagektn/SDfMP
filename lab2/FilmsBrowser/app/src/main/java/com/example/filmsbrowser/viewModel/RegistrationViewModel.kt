package com.example.filmsbrowser.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    private val _loginValidation = MutableLiveData<Boolean>()
    val loginValidation: LiveData<Boolean> = _loginValidation

    private val _emailValidation = MutableLiveData<Boolean>()
    val emailValidation: LiveData<Boolean> = _emailValidation

    private val _passwordValidation = MutableLiveData<Boolean>()
    val passwordValidation: LiveData<Boolean> = _passwordValidation

    private val _loginInUse = MutableLiveData<Boolean>()
    val loginInUse: LiveData<Boolean> = _loginInUse

    fun registerUser(login: String, email: String, password: String) {
        if (login.isEmpty() || login.length < 4) {
            _loginValidation.value = false
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailValidation.value = false
            return
        }

        if (password.isEmpty() || password.length < 8) {
            _passwordValidation.value = false
            return
        }

        val usersRef = database.getReference("users")
        val query = usersRef.orderByChild("login").equalTo(login)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful && !task.result.exists()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        addUser(login, email)
                    } else {
                        _registrationSuccess.value = false
                    }
                }
            } else {
                _loginInUse.value = true
            }
        }
    }

    private fun addUser(login: String, email: String) {
        val users = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid)

        val newUser = User(login, email)
        users.setValue(newUser).addOnCompleteListener { task ->
            _registrationSuccess.value = task.isSuccessful
        }
    }
}
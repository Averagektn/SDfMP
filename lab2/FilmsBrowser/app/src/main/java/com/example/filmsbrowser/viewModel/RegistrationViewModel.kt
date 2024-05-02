package com.example.filmsbrowser.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    var login: String = ""
    var email: String = ""
    var password: String = ""

    val registrationSuccess = MutableLiveData<Boolean>()
    val loginValidation = MutableLiveData<Boolean>()
    val emailValidation = MutableLiveData<Boolean>()
    val passwordValidation = MutableLiveData<Boolean>()
    val loginInUse = MutableLiveData<Boolean>()

    fun registerUser() {
        if (validateInput()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser!!
                        val userData = mapOf(
                            "login" to login,
                            "email" to email
                        )
                        database.child("users").child(user.uid).setValue(userData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    registrationSuccess.value = true
                                } else {
                                    registrationSuccess.value = false
                                }
                            }
                    } else {
                        if (task.exception?.message?.contains("email address is already in use") == true) {
                            loginInUse.value = true
                        } else {
                            registrationSuccess.value = false
                        }
                    }
                }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (login.length < 4) {
            loginValidation.value = false
            isValid = false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailValidation.value = false
            isValid = false
        }

        if (password.length < 8) {
            passwordValidation.value = false
            isValid = false
        }

        return isValid
    }
}
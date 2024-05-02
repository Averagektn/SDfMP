package com.example.filmsbrowser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthorizationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _authorizationResult = MutableLiveData<Boolean>()
    val authorizationResult: LiveData<Boolean> get() = _authorizationResult

    fun authorizeUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authorizationResult.value = task.isSuccessful
            }
    }
}
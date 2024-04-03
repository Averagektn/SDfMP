package com.example.filmsbrowser.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthorizationViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val authorizationResult: MutableLiveData<Boolean> = MutableLiveData()

    fun authorizeUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            authorizationResult.value = task.isSuccessful
        }
    }
}
package com.example.filmsbrowser.viewModel

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.InputFilter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.filmsbrowser.activity.AuthorizationActivity
import com.example.filmsbrowser.activity.ProfileActivity
import com.example.filmsbrowser.activity.RegistrationActivity
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    val user = MutableLiveData<User?>()
    val error = MutableLiveData<String>()
    private val imageUri = MutableLiveData<Uri>()

    fun loadProfile(context: ProfileActivity) {
        database.getReference("users").child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(User::class.java)
                user.value = userData

                // Download and set the profile picture
                storage.getReference("profile_photos/${auth.currentUser!!.uid}").downloadUrl
                    .addOnSuccessListener { uri ->
                        Glide
                            .with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerInside()
                            .into(context.binding.userImage)
                    }

            }

            override fun onCancelled(error: DatabaseError) {
                this@ProfileViewModel.error.value = error.message
            }
        })
    }

    fun showEditText(childField: String, caption: String, textView: TextView, context: ProfileActivity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(caption)

        val input = EditText(context)
        val maxLength = 20
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        input.filters = filters
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val newValue = input.text.toString()
            database.getReference("users").child(auth.currentUser!!.uid).child(childField.lowercase())
                .setValue(newValue)
                .addOnCompleteListener {
                    textView.text = String.format("$caption: $newValue")
                }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun showDatePicker(childField: String, caption: String, textView: TextView, context: ProfileActivity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(caption)

        val datePicker = DatePicker(context)
        builder.setView(datePicker)

        builder.setPositiveButton("OK") { _, _ ->
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year
            val date = "$day.$month.$year"

            database.getReference("users").child(auth.currentUser!!.uid).child(childField.lowercase())
                .setValue(date)
                .addOnCompleteListener {
                    textView.text = String.format("$caption: $date")
                }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun showDeleteUserDialog(context: ProfileActivity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select action")

        builder.setPositiveButton("Log out") { _, _ ->
            auth.signOut()
            val intent = Intent(context, AuthorizationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        builder.setNegativeButton("Delete user forever") { _, _ ->
            val uid = auth.currentUser!!.uid
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        database.getReference("users/$uid").removeValue()
                        storage.getReference("profile_photos/$uid").delete()
                        database.getReference("favored/$uid").removeValue()
                        auth.signOut()
                        val intent = Intent(context, RegistrationActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun pickImageIntent(imageActivityResult: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        imageActivityResult.launch(intent)
    }

    fun saveImage(imageUri: Uri, context: ProfileActivity) {
        this.imageUri.value = imageUri
        val storageRef = storage.getReference("profile_photos/${auth.currentUser!!.uid}")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide
                        .with(context)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerInside()
                        .into(context.binding.userImage)
                }
            }
    }
}
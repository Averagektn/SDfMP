package com.example.filmsbrowser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.filmsbrowser.databinding.ActivityProfileBinding
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityProfileBinding

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.btnToFavored.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFilmsList.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FilmsListActivity::class.java)
            startActivity(intent)
        }

        binding.userImage.setOnClickListener {
            pickImageIntent()
        }

        binding.btnEditLogin.setOnClickListener {
            showEditText("Login", binding.tvUserLogin)
        }

        binding.btnEditGender.setOnClickListener {
            showEditText("Gender", binding.tvUserGender)
        }

        binding.btnEditCountry.setOnClickListener {
            showEditText("Country", binding.tvUserCountry)
        }

        binding.btnEditSurname.setOnClickListener {
            showEditText("Surname", binding.tvUserSurname)
        }

        binding.btnEditUserName.setOnClickListener {
            showEditText("Username", binding.tvUserName)
        }

        binding.btnEditBirthDate.setOnClickListener {
            showEditText("BirthDate", binding.tvUserBirthDate)
        }

        binding.btnEditPatronymic.setOnClickListener {
            showEditText("Patronymic", binding.tvUserPatronymic)
        }

        binding.btnEditUserInformation.setOnClickListener {
            showEditText("Information", binding.tvUserInformation)
        }

        binding.btnLogOut.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select action")

            builder.setPositiveButton("Log out") { _, _ ->
                auth.signOut()
                val intent = Intent(this, AuthorizationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
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
                            val intent = Intent(this, RegistrationActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error deleting user", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            val dialog = builder.create()
            dialog.show()
        }

        showData()
    }

    private fun showEditText(childField: String, textView: TextView) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(childField)

        val input = EditText(this)
        val maxLength = 20
        val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        input.filters = filters
        builder.setView(input)

        builder.setPositiveButton("ОК") { _, _ ->
            val newValue = input.text.toString()
            database.getReference("users").child(auth.currentUser!!.uid).child(childField.lowercase())
                .setValue(newValue)
                .addOnCompleteListener {
                    textView.text = String.format("$childField: $newValue")
                }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun saveImage() {
        val storageRef = storage.getReference("profile_photos/${auth.currentUser!!.uid}")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide
                        .with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.userImage)
                }
            }
    }


    private fun showData() {
        val ref = database.getReference("users").child(auth.currentUser!!.uid)

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getValue(User::class.java)

                binding.tvUserLogin.text = String.format("Login: %s", model?.login)
                binding.tvUserEmail.text = String.format("Email: %s", model?.email)
                binding.tvUserName.text = String.format("Username: %s", model?.username)
                binding.tvUserGender.text = String.format("Gender: %s", model?.gender)
                binding.tvUserCountry.text = String.format("Country: %s", model?.country)
                binding.tvUserPatronymic.text = String.format("Patronymic: %s", model?.patronymic)
                binding.tvUserSurname.text = String.format("Surname: %s", model?.surname)
                binding.tvUserInformation.text = String.format("Info: %s", model?.information)
                binding.tvUserBirthDate.text = String.format("BirthDate: %s", model?.birthDate)
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed loading profile", Toast.LENGTH_SHORT).show()
            }

        })

        storage.getReference("profile_photos/${auth.currentUser!!.uid}").downloadUrl
            .addOnSuccessListener { uri ->
                Glide
                    .with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.userImage)
            }
    }

    private val imageActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            imageUri = it.data!!.data!!
            saveImage()
        }
    }

    private fun pickImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        imageActivityResult.launch(intent)
    }
}
package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityProfileBinding
import com.example.filmsbrowser.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    lateinit var binding: ActivityProfileBinding
    private lateinit var imageActivityResult: ActivityResultLauncher<Intent>
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        viewModel = ProfileViewModel()

        imageActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                viewModel.saveImage(it.data!!.data!!, this)
            }
        }

        binding.btnToFavored.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFilmsList.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FilmsListActivity::class.java)
            startActivity(intent)
        }

        binding.userImage.setOnClickListener {
            viewModel.pickImageIntent(imageActivityResult)
        }

        binding.btnEditLogin.setOnClickListener {
            viewModel.showEditText("Login", "Login", binding.tvUserLogin, this)
        }

        binding.btnEditGender.setOnClickListener {
            viewModel.showEditText("Gender", "Gender", binding.tvUserGender, this)
        }

        binding.btnEditCountry.setOnClickListener {
            viewModel.showEditText("Country", "Country", binding.tvUserCountry, this)
        }

        binding.btnEditSurname.setOnClickListener {
            viewModel.showEditText("Surname", "Surname", binding.tvUserSurname, this)
        }

        binding.btnEditUserName.setOnClickListener {
            viewModel.showEditText("Username", "Name", binding.tvUserName, this)
        }

        binding.btnEditBirthDate.setOnClickListener {
            viewModel.showDatePicker("BirthDate", "Birth date", binding.tvUserBirthDate, this)
        }

        binding.btnEditPatronymic.setOnClickListener {
            viewModel.showEditText("Patronymic", "Patronymic", binding.tvUserPatronymic, this)
        }

        binding.btnEditUserInformation.setOnClickListener {
            viewModel.showEditText("Information", "Information", binding.tvUserInformation, this)
        }

        binding.btnLogOut.setOnClickListener {
            viewModel.showDeleteUserDialog(this)
        }

        viewModel.user.observe(this) { user ->
            if (user != null) {
                binding.tvUserLogin.text = String.format("Login: %s", user.login)
                binding.tvUserEmail.text = String.format("Email: %s", user.email)
                binding.tvUserName.text = String.format("Username: %s", user.username)
                binding.tvUserGender.text = String.format("Gender: %s", user.gender)
                binding.tvUserCountry.text = String.format("Country: %s", user.country)
                binding.tvUserPatronymic.text = String.format("Patronymic: %s", user.patronymic)
                binding.tvUserSurname.text = String.format("Surname: %s", user.surname)
                binding.tvUserInformation.text = String.format("Info: %s", user.information)
                binding.tvUserBirthDate.text = String.format("Birth date: %s", user.birthDate)
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadProfile(this)
    }
}
package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityRegistrationBinding
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnSignUp.setOnClickListener {
            val login = binding.etLogin.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (login.isEmpty() || login.length < 4) {
                Toast.makeText(applicationContext, "Invalid username", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty() || password.length < 8) {
                Toast.makeText(applicationContext, "Invalid password", Toast.LENGTH_SHORT).show()
            } else {
                registration(login, email, password)
            }
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, AuthorizationActivity::class.java)
            startActivity(intent)
        }

        //initializeDbWithFilms()
    }


    private fun registration(login: String, email: String, password: String) {
        val usersRef = database.getReference("users")
        val query = usersRef.orderByChild("login").equalTo(login)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful && !task.result.exists()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        addUser(login, email)
                    } else {
                        Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "This login is already in use", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser(login: String, email: String) {
        val users = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid)

        val newUser = User(login, email)
        users.setValue(newUser).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this@RegistrationActivity, FilmsListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeDbWithFilms() {
        val node = database.getReference("films")
        node.push().setValue(
            Film(
                "The Captain",
                listOf("Drama", "History", "Thriller"),
                "In the last moments of World War II, a young German soldier fighting for survival finds a Nazi captain's uniform. Impersonating an officer, the man quickly takes on the monstrous identity of the perpetrators he's escaping from."
            )
        )
        node.push().setValue(
            Film(
                "The Hunt",
                listOf("Drama"),
                "A teacher lives a lonely life, all the while struggling over his son's custody. His life slowly gets better as he finds love and receives good news from his son, but his new luck is about to be brutally shattered by an innocent little lie."
            )
        )
        node.push().setValue(
            Film(
                "Killers of the Flower Moon",
                listOf("Crime", "Drama", "History"),
                "When oil is discovered in 1920s Oklahoma under Osage Nation land, the Osage people are murdered one by one - until the FBI steps in to unravel the mystery."
            )
        )
        node.push().setValue(
            Film(
                "The Lighthouse",
                listOf("Drama", "Fantasy", "Horror"),
                "Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s."
            )
        )
        node.push().setValue(
            Film(
                "Oppenheimer",
                listOf("Biography", "Drama", "History"),
                "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb."
            )
        )
        node.push().setValue(
            Film(
                "Eternal Sunshine of the Spotless Mind",
                listOf("Drama", "Romance", "Sci-Fi"),
                "When their relationship turns sour, a couple undergoes a medical procedure to have each other erased from their memories for ever."
            )
        )
        node.push().setValue(
            Film(
                "The Lovely Bones",
                listOf("Drama", "Fantasy", "Thriller"),
                "Centers on a young girl who has been murdered and watches over her family - and her killer - from purgatory. She must weigh her desire for vengeance against her desire for her family to heal."
            )
        )
        node.push().setValue(
            Film(
                "Barbie",
                listOf("Adventure", "Comedy", "Fantasy"),
                "Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans."
            )
        )
        node.push().setValue(
            Film(
                "Drive",
                listOf("Action", "Drama"),
                "A mysterious Hollywood action film stuntman gets in trouble with gangsters when he tries to help his neighbor's husband rob a pawn shop while serving as his getaway driver."
            )
        )
        node.push().setValue(
            Film(
                "Flight",
                listOf("Drama", "Thriller"),
                "Troubling questions arise after airline pilot Whip Whitaker makes a miracle landing after a mid-air catastrophe."
            )
        )
        node.push().setValue(
            Film(
                "Zodiac",
                listOf("Crime", "Drama", "Mystery"),
                "Between 1968 and 1983, a San Francisco cartoonist becomes an amateur detective obsessed with tracking down the Zodiac Killer, an unidentified individual who terrorizes Northern California with a killing spree."
            )
        )
        node.push().setValue(
            Film(
                "Blade Runner 2049",
                listOf("Action", "Drama", "Mystery"),
                "Young Blade Runner K's discovery of a long-buried secret leads him to track down former Blade Runner Rick Deckard, who's been missing for thirty years."
            )
        )
        node.push().setValue(
            Film(
                "Nightcrawler",
                listOf("Crime", "Drama", "Thriller"),
                "When Louis Bloom, a con man desperate for work, muscles into the world of L.A. crime journalism, he blurs the line between observer and participant to become the star of his own story."
            )
        )
        node.push().setValue(
            Film(
                "Donnie Darko",
                listOf("Drama", "Mystery", "Sci-Fi"),
                "After narrowly escaping a bizarre accident, a troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes."
            )
        )
        node.push().setValue(
            Film(
                "Bullet Train",
                listOf("Action", "Comedy", "Thriller"),
                "Five assassins aboard a swiftly-moving bullet train find out that their missions have something in common."
            )
        )
        node.push().setValue(
            Film(
                "Fight Club",
                listOf("Drama"),
                "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more."
            )
        )
        node.push().setValue(
            Film(
                "Prisoners",
                listOf("Crime", "Drama", "Mystery"),
                "When Keller Dover's daughter and her friend go missing, he takes matters into his own hands as the police pursue multiple leads and the pressure mounts."
            )
        )
        node.push().setValue(
            Film(
                "The Basketball Diaries",
                listOf("Biography", "Crime", "Drama"),
                "A teenager finds his dreams of becoming a basketball star threatened after he free falls into the harrowing world of drug addiction."
            )
        )
        node.push().setValue(
            Film(
                "The Ballad of Buster Scruggs",
                listOf("Comedy", "Drama", "Musical"),
                "Six tales of life and violence in the Old West, following a singing gunslinger, a bank robber, a traveling impresario, an elderly prospector, a wagon train, and a perverse pair of bounty hunters."
            )
        )
        node.push().setValue(
            Film(
                "Unthinkable",
                listOf("Crime", "Drama", "Thriller"),
                "Follows a black-ops interrogator and an F.B.I. agent who try to press a suspect terrorist into divulging the location of three nuclear weapons set to detonate in the U.S."
            )
        )
        node.push().setValue(
            Film(
                "Se7en",
                listOf("Crime", "Drama", "Mystery"),
                "Two detectives, a rookie and a veteran, hunt a serial killer who uses the seven deadly sins as his motives."
            )
        )
        node.push().setValue(
            Film(
                "The Shining",
                listOf("Drama", "Horror"),
                "A family heads to an isolated hotel for the winter where a sinister presence influences the father into violence, while his psychic son sees horrific forebodings from both past and future."
            )
        )
        node.push().setValue(
            Film(
                "Dune",
                listOf("Action", "Adventure", "Drama"),
                "A noble family becomes embroiled in a war for control over the galaxy's most valuable asset while its heir becomes troubled by visions of a dark future."
            )
        )
        node.push().setValue(
            Film(
                "Call Me by Your Name",
                listOf("Drama", "Romance"),
                "In 1980s Italy, romance blossoms between a seventeen-year-old student and the older man hired as his father's research assistant."
            )
        )
        node.push().setValue(
            Film(
                "Fargo",
                listOf("Crime", "Drama", "Thriller"),
                "Various chronicles of deception, intrigue, and murder in and around frozen Minnesota. All of these tales mysteriously lead back one way or another to Fargo, North Dakota."
            )
        )
        node.push().setValue(
            Film(
                "El Camino: A Breaking Bad Movie",
                listOf("Action", "Crime", "Drama"),
                "Fugitive Jesse Pinkman runs from his captors, the law, and his past."
            )
        )
        node.push().setValue(
            Film(
                "The Platform",
                listOf("Horror", "Sci-Fi", "Thriller"),
                "A vertical prison with one cell per level. Two people per cell. Only one food platform and two minutes per day to feed. An endless nightmare trapped in The Hole."
            )
        )
        node.push().setValue(
            Film(
                "The Boat",
                listOf("Drama", "War"),
                "A German U-boat stalks the frigid waters of the North Atlantic as its young crew experience the sheer terror and claustrophobic life of a submariner in World War II."
            )
        )
        node.push().setValue(
            Film(
                "Requiem for a Dream",
                listOf("Drama"),
                "The drug-induced utopias of four Coney Island people are shattered when their addictions run deep."
            )
        )
        node.push().setValue(
            Film(
                "A Serbian Film",
                listOf("Horror", "Mystery", "Thriller"),
                "An aging porn star agrees to participate in an \"art film\" in order to make a clean break from the business, only to discover that he has been drafted into making a pedophilia and necrophilia themed snuff film."
            )
        )
    }
}

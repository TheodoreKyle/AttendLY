package com.example.attendly

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_account)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etAge = findViewById<EditText>(R.id.etAge)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnCreate = findViewById<Button>(R.id.createAccount)
        val btnLogin = findViewById<TextView>(R.id.btnLogin)

        setupGenderSpinner(spGender)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnCreate.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val ageText = etAge.text.toString().trim()
            val gender = spGender.selectedItem.toString()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            // 1. Full Name validation (letters, spaces, ñ/Ñ only; must start with uppercase)
            val fullNameRegex = Regex("^[A-ZÑ][a-zA-ZñÑ\\s]*$")
            if (fullName.isEmpty() || !fullNameRegex.matches(fullName)) {
                etFullName.error = "Must start with a capital letter and contain letters only (ñ/Ñ allowed)"
                etFullName.requestFocus()
                return@setOnClickListener
            }

            // 2. Username validation (letters and numbers only)
            val usernameRegex = Regex("^[a-zA-Z0-9]+$")
            if (username.isEmpty() || !usernameRegex.matches(username)) {
                etUsername.error = "Username must contain letters and numbers only"
                etUsername.requestFocus()
                return@setOnClickListener
            }

            // 3. Age validation (positive integer only)
            val age = ageText.toIntOrNull()
            if (age == null || age <= 0 || age > 120) {
                etAge.error = "Please enter a valid positive age"
                etAge.requestFocus()
                return@setOnClickListener
            }

            // 4. Gender validation
            if (spGender.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select a gender option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 5. Email validation
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Please enter a valid email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // 6. Password validation
            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Disable button to prevent duplicate submissions
            btnCreate.isEnabled = false

            // 7. Create Firebase Auth account
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val userData = hashMapOf(
                                "fullName" to fullName,
                                "username" to username,
                                "age" to age,
                                "gender" to gender,
                                "email" to email,
                                "role" to "user"
                            )

                            // 8. Save profile to Firestore under users/{uid}
                            db.collection("users").document(uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainPage::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    btnCreate.isEnabled = true
                                    Toast.makeText(this, "Failed to save profile: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        btnCreate.isEnabled = true
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun setupGenderSpinner(spinner: Spinner) {
        val options = resources.getStringArray(R.array.gender_options)
        val jerseyFont = ResourcesCompat.getFont(this, R.font.jersey10)

        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
            override fun isEnabled(position: Int): Boolean {
                // Disable "Select Gender" in the dropdown list
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.typeface = jerseyFont
                view.textSize = 22f
                view.setTextColor(if (position == 0) Color.parseColor("#4A6B7C") else Color.WHITE)
                view.setPadding(24, 0, 24, 0)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.typeface = jerseyFont
                view.textSize = 22f
                view.setPadding(32, 24, 32, 24)
                view.setTextColor(if (position == 0) Color.parseColor("#4A6B7C") else Color.WHITE)
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
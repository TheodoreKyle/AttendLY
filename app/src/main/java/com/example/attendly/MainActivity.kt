package com.example.attendly

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        auth = FirebaseAuth.getInstance()

        val tvNoAccount = findViewById<TextView>(R.id.tvNoAccount)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val etLoginEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etLoginPassword = findViewById<EditText>(R.id.etLoginPassword)

        tvNoAccount.setOnClickListener {
            val intent = Intent(this, RegisterAccount::class.java)
            startActivity(intent)
        }

        btnSubmit.setOnClickListener {
            val email = etLoginEmail.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, SuccessfulLogin::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        startActivity(intent)
                        finish()
                    } else {
                        val exception = task.exception
                        when (exception) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(this, "Email not found on Firebase", Toast.LENGTH_SHORT).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, "Login Failed: ${exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }
    }
}
package com.example.attendly

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SuccessfulLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_successfullogin)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvDisplayUsername = findViewById<TextView>(R.id.tvDisplayUsername)

        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Unknown User"

        tvDisplayUsername.text = "Successfully Logged In!\nUser: $userEmail"

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
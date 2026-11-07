package com.example.attendly

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.content.*

class SuccessfulLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_successfullogin)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvDisplayUsername = findViewById<TextView>(R.id.tvDisplayUsername)

        tvDisplayUsername.text = "Successfully Logged In! \nUser: thisuser"


        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}
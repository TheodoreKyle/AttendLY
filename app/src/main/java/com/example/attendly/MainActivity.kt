package com.example.attendly

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import android.widget.*
import android.content.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val tvNoAccount = findViewById<TextView>(R.id.tvNoAccount)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        tvNoAccount.setOnClickListener {

            val intent = Intent(this, RegisterAccount::class.java)

            startActivity(intent)
        }

        btnSubmit.setOnClickListener {

            val intent = Intent(this, SuccessfulLogin::class.java)
            startActivity(intent)
            finish()
        }



    }
}
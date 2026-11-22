package com.example.attendly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        val btnLogout = findViewById<Button>(R.id.btnLogout) // match your ID in activity_profile_page.xml
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }
    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnCancel = dialog.findViewById<Button>(R.id.btnDialogCancel)
        val btnConfirmLogout = dialog.findViewById<Button>(R.id.btnDialogLogout)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
}
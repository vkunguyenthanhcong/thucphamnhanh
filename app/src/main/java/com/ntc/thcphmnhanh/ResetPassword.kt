package com.ntc.thcphmnhanh

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class ResetPassword : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button


        val submit = findViewById<Button>(R.id.submit)

        submit.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailrs).getText().toString()
            val auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Vui lòng kiểm tra email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                val intent = Intent(this, Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
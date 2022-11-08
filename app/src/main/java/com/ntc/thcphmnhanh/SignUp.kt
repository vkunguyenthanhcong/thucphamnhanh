package com.ntc.thcphmnhanh

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button

        val btndangnhap = findViewById<ConstraintLayout>(R.id.dangnhap)
        btndangnhap.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        Dangky()

    }

    fun Dangky(){
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val btndangky = findViewById<ImageButton>(R.id.btndangky)
            btndangky.setOnClickListener {
                val password: String = findViewById<EditText>(R.id.password).getText().toString()
                val repassword: String = findViewById<EditText>(R.id.repassword).getText().toString()
                val email: String = findViewById<EditText>(R.id.username).getText().toString()
                if (password != repassword || email.length == 0){
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }else{
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Thực phẩm nhanh")
                    progressDialog.setMessage("Đang đăng ký...")
                    progressDialog.show()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val uid = user?.uid
                                val intent = Intent(this, Login::class.java)
                                intent.putExtra("uid", uid!!)
                                intent.putExtra("email", email)
                                user?.sendEmailVerification()
                                Firebase.auth.signOut()
                                startActivity(intent)
                                progressDialog.hide()
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(baseContext, "Tài khoản đã được đăng kí trước đó",
                                    Toast.LENGTH_SHORT).show()
                                    progressDialog.hide()
                            }
                        }
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
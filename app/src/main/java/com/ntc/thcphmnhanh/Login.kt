package com.ntc.thcphmnhanh

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val quenmk = findViewById<TextView>(R.id.text3)
        quenmk.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
            finish()
        }

        val btndangky = findViewById<ConstraintLayout>(R.id.dangky)
        btndangky.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        username.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        password.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })


        checkuser()
        val btndangnhap = findViewById<ImageButton>(R.id.btndangnhap)
        btndangnhap.setOnClickListener {
            dangnhap()
        }
        val uid: String = intent.getStringExtra("uid").toString()
        val email : String = intent.getStringExtra("email").toString()
        val db = FirebaseFirestore.getInstance()
        if (uid != "null"){
            val data: MutableMap<String, Any> = HashMap()
            data["hoten"] =  ""
            data["diachi"] = ""
            data["sodienthoai"] = ""
            data["gioitinh"] = ""
            data["id"] = uid
            data["permission"] = "user"
            data["link"] =  "http://www.nuockhoangtinhkhiet24h.com/upload/img/inuser/Avatar-Facebook-tr%E1%BA%AFng.jpg"
            data["email"] = email

            db.collection("users").document(uid!!)
                .set(data)
        }

    }

    fun checkuser(){
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
    fun dangnhap() {
        val user = FirebaseAuth.getInstance().currentUser

        val email: String = findViewById<EditText>(R.id.username).getText().toString()
        val password: String = findViewById<EditText>(R.id.password).getText().toString()
        if (email.length == 0 || password.length == 0 ){
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }else{
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Thực phẩm nhanh")
            progressDialog.setMessage("Đang đăng nhập...")
            progressDialog.show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val emailVerified = user!!.isEmailVerified
                        if (emailVerified){
                            intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Tài khoản của bạn chưa được xác minh", Toast.LENGTH_SHORT).show()
                            progressDialog.hide()
                            user?.sendEmailVerification()
                            Firebase.auth.signOut()
                            Toast.makeText(this, "Đã gửi lại email xác minh. Vui lòng vào email để kiểm tra", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show()
                        progressDialog.hide()
                    }
                }
        }

    }

}
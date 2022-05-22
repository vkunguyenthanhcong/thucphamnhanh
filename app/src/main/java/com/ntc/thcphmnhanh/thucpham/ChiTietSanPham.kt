package com.ntc.thcphmnhanh.thucpham

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.databinding.ActivityChiTietSanPhamBinding
import com.squareup.picasso.Picasso

class ChiTietSanPham : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    private lateinit var id: String
    private lateinit var binding: ActivityChiTietSanPhamBinding
    private lateinit var addcart : Button
    private lateinit var tongso : String
    private lateinit var linkanh : String
    private lateinit var ten : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChiTietSanPhamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        id = intent.getStringExtra("id").toString()

        val imageView = binding.imagebanner
        val giasp = binding.giasp
        val tensp = binding.tensp
        val motasp = binding.motasp

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("danhsach").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    giasp.setText(document.data?.getValue("gia").toString())
                    tensp.setText(document.data?.getValue("ten").toString())
                    motasp.setText(document.data?.getValue("mota").toString())
                    linkanh = document.data?.getValue("linkanh").toString()
                    Picasso.get().load(linkanh).placeholder(R.drawable.banner_home).error(R.drawable.banner_home).into(imageView)
                }
            }



        val tang = binding.tang
        val giam = binding.giam

        tang.setOnClickListener {
            val txtsoluong = binding.soluong
            var tong : Int = txtsoluong.text.toString().toInt().plus(1)
            tongso = tong.toString()
            txtsoluong.setText(tongso)
        }
        giam.setOnClickListener {
            val txtsoluong = binding.soluong
            if (txtsoluong.text.toString() != "0"){
                var tong : Int = txtsoluong.text.toString().toInt().minus(1)
                tongso = tong.toString()
                txtsoluong.setText(tongso)
            }
        }

        addcart = binding.addcart
        addcart.setOnClickListener {

            var tongsoluong = binding.soluong.text.toString()

            val user = Firebase.auth.currentUser
            val uid = user?.uid
            val sodienthoai : Int = 84
            database = FirebaseDatabase.getInstance().getReference("cart")
            val length = 20
            getRandomString(length)
            val randomString = getRandomString(length)
            val data = mapOf<String, Any>(
                "gia" to giasp.text.toString().toInt(),
                "ten" to tensp.text,
                "mota" to motasp.text,
                "id" to uid!!,
                "soluong" to tongsoluong.toInt(),
                "tinhtrang" to "Cart",
                "linkanh" to linkanh,
                "idsanpham" to id,
                "idcart" to randomString,
                "diachi" to "",
                "sodienthoai" to sodienthoai
            )
            database.child(randomString).updateChildren(data).addOnCompleteListener {
                val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ting)
                mediaPlayer.start()
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()

            }
        }

    }


    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }

}

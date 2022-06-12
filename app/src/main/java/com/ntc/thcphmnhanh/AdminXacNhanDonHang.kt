package com.ntc.thcphmnhanh

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.databinding.ActivityAdminXacNhanDonHangBinding
import com.ntc.thcphmnhanh.databinding.ActivityXacNhanDonHang2Binding
import com.ntc.thcphmnhanh.databinding.FragmentGioHangBinding
import com.ntc.thcphmnhanh.home.GioHangAdapter
import com.ntc.thcphmnhanh.ui.admin.GioHang
import com.ntc.thcphmnhanh.ui.cart.GioHangFragment
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay as delay1

class AdminXacNhanDonHang : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var binding: ActivityAdminXacNhanDonHangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminXacNhanDonHangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val idcart : String = intent.getStringExtra("idcart").toString()

        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //current time
                    val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
                    val currentDate = sdf.format(Date())

                    Picasso.get().load(snapshot.child("linkanh").value.toString()).placeholder(R.drawable.logo).into(binding.image)

                    binding.tensp.setText(snapshot.child("ten").value.toString())

                    binding.giasp.setText(snapshot.child("gia").value.toString())

                    binding.soluong.setText(snapshot.child("soluong").value.toString())

                    binding.tgdathang.setText(snapshot.child("ngaydathang").value.toString())

                    val tongtien : Int = binding.soluong.text.toString().toInt() * binding.giasp.text.toString().toInt()
                    val tinhtrang  = snapshot.child("tinhtrang").value.toString()
                    if (tinhtrang == "Cart"){
                        binding.tinhtrang.setText("Đơn hàng mới")
                    }else if (tinhtrang == "Đã đặt hàng"){
                        binding.tinhtrang.setText("Đã đặt hàng")
                        binding.tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }else if (tinhtrang == "Đã gửi hàng"){
                        binding.tinhtrang.setText("Đã gửi hàng")
                        binding.tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }
                    binding.tongtien.setText(tongtien.toString())
                    binding.giatien.setText(tongtien.toString())

                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(uid!!)
                    docRef.get().addOnSuccessListener { document ->
                        val txtdiachi : String = document.data?.getValue("diachi").toString()
                        binding.diachi.setText(txtdiachi)
                        val txtsodienthoai : String = document.data?.getValue("sodienthoai").toString()
                        binding.sodienthoai.setText(txtsodienthoai)
                    }
                    binding.huy.setOnClickListener {

                        val database = Firebase.database.reference
                        database.child("cart").child(idcart).removeValue().addOnCompleteListener {
                            val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ting)
                            mediaPlayer.start()
                            Toast.makeText(applicationContext, "Hủy thành công", Toast.LENGTH_SHORT).show()
                            Handler().postDelayed({
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                            }, 1000)


                        }
                    }
                    binding.guihang.setOnClickListener {
                        dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
                        val data = mapOf<String, Any>(
                            "tinhtrang" to "Đã gửi hàng",
                            "ngaygiaohang" to currentDate
                        )
                        dbref.updateChildren(data).addOnCompleteListener {
                            val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ting)
                            mediaPlayer.start()
                            Toast.makeText(applicationContext, "Đặt gửi thành công", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

}
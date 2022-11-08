package com.ntc.thcphmnhanh.ui.xacnhan

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ntc.thcphmnhanh.R
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_verify.*
import org.w3c.dom.Text
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailVerify : Fragment() {
    private lateinit var dbref : DatabaseReference
    private val args : DetailVerifyArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_verify, container, false)

        val idcart = args.id

        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //current time
                    val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
                    val currentDate = sdf.format(Date())

                    Picasso.get().load(snapshot.child("linkanh").value.toString()).placeholder(R.drawable.logo).into(view.findViewById<ImageView>(R.id.imagesp))

                    view.findViewById<TextView>(R.id.tensp).setText(snapshot.child("ten").value.toString())
                    val giasp = view.findViewById<TextView>(R.id.giasp)
                    val COUNTRY : String = "VN"
                    val LANGUAGE : String =  "vi"
                    val gia = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("gia").value)
                    giasp.setText(gia.toString())
                    val soluong = view.findViewById<TextView>(R.id.soluong)
                    soluong.setText(snapshot.child("soluong").value.toString())

                    view.findViewById<TextView>(R.id.tgdathang).setText(snapshot.child("ngaydathang").value.toString())
                    view.findViewById<TextView>(R.id.tgguihang).setText(snapshot.child("ngaygiaohang").value.toString())
                    view.findViewById<TextView>(R.id.tgnhanhang).setText(snapshot.child("ngaynhanhang").value.toString())
                    view.findViewById<TextView>(R.id.tonggiatien).setText(NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("tongtien").value).toString())
                    val tinhtrang  = view.findViewById<TextView>(R.id.tinhtrang)
                    tinhtrang.setText(snapshot.child("tinhtrang").value.toString())

                    val huydon = view.findViewById<Button>(R.id.xoasp)
                    view.findViewById<TextView>(R.id.diachi).setText(snapshot.child("diachi").value.toString())
                    view.findViewById<TextView>(R.id.phone).setText(snapshot.child("sodienthoai").value.toString())
                    if (tinhtrang.text == "Đã hủy đơn" || tinhtrang.text == "Đã nhận hàng" || tinhtrang.text == "Đã gửi hàng" || tinhtrang.text == "Cart"){
                        huydon.visibility = View.GONE
                        mainxacnhan.visibility = View.GONE
                    }

                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


        return view
    }

}
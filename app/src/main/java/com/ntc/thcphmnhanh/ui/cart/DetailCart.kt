package com.ntc.thcphmnhanh.ui.cart

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.DetailProductArgs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_cart.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailCart : Fragment() {
    private lateinit var dbref : DatabaseReference
    private val args : DetailCartArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_cart, container, false)
        val idcart = args.id

        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //image
                    Picasso.get().load(snapshot.child("linkanh").value.toString()).placeholder(R.drawable.logo).into(view.findViewById<ImageView>(R.id.imagesp))
                    //tên sản phẩm
                    tensp.setText(snapshot.child("ten").value.toString())
                    //giá sản phẩm
                    val giasp = view.findViewById<TextView>(R.id.giasp)

                    val COUNTRY : String = "VN"
                    val LANGUAGE : String =  "vi"
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("gia").value)
                    giasp.setText(numberFormat.toString())
                    //số lượng sản phẩm
                    val soluong = view.findViewById<TextView>(R.id.soluong)
                    soluong.setText(snapshot.child("soluong").value.toString())
                    //ngày đặt hàng
                    tgdathang.setText(snapshot.child("ngaydathang").value.toString())
                    //tổng tiền phải thanh toán
                    val tongtien : Int = snapshot.child("soluong").value.toString().toInt() * snapshot.child("gia").value.toString().toInt()
                    val tongtienFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(tongtien)
                    //tình trạng đơn hàng
                    val tinhtrang  = snapshot.child("tinhtrang").value.toString()
                    val tt = view.findViewById<TextView>(R.id.tinhtrang)
                    if (tinhtrang == "Cart"){
                        tt.setText("• Đơn hàng mới")
                        tt.setTextColor(Color.parseColor("#ffff00"))
                    }else if (tinhtrang == "Đã đặt hàng"){
                        tt.setText("• Đã đặt hàng")
                        tt.setTextColor(Color.parseColor("#32cd32"))
                    }
                    view.findViewById<TextView>(R.id.tongtien).setText(tongtienFormat.toString())

                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(uid!!)
                    docRef.get().addOnSuccessListener { document ->
                        val txtdiachi : String = document.data?.getValue("diachi").toString()
                        diachi.setText(txtdiachi)
                        val txtsodienthoai : String = document.data?.getValue("sodienthoai").toString()
                        phone.setText(txtsodienthoai)
                    }
                    huycart.setOnClickListener {
                        dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
                        val data = mapOf<String, Any>(
                            "id" to ""

                        )
                        dbref.updateChildren(data).addOnCompleteListener {
                            Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_LONG).show()
                            findNavController().navigate(DetailCartDirections.actionNavXacnhancartToNavCart())
                        }
                    }
                    xacnhan.setOnClickListener {
                        val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
                        val currentDate = sdf.format(Date())
                        dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
                        val data = mapOf<String, Any>(
                            "tinhtrang" to "Đã đặt hàng",
                            "diachi" to diachi.text.toString(),
                            "sodienthoai" to phone.text.toString().toInt(),
                            "ngaydathang" to currentDate,
                            "tongtien" to tongtien

                        )
                        dbref.updateChildren(data).addOnCompleteListener {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.ting)
                            mediaPlayer.start()
                            Toast.makeText(context, "Đặt hàng thành công", Toast.LENGTH_LONG).show()
                            findNavController().navigate(DetailCartDirections.actionNavXacnhancartToNavCart())
                        }
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

        return view
    }

}
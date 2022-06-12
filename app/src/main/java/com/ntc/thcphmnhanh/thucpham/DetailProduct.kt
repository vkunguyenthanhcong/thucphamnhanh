package com.ntc.thcphmnhanh.thucpham

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_detail_product.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailProduct : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var linkanh : String
    private lateinit var gia : String
    private val args : DetailProductArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        val id : String = args.id.toString()

        val imageView = view.findViewById<ImageView>(R.id.imagebanner)
        val giasp = view.findViewById<TextView>(R.id.giasanpham)
        val tensanpham = view.findViewById<TextView>(R.id.tensp)
        val mota = view.findViewById<TextView>(R.id.motasp)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("danhsach").document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val COUNTRY : String = "VN"
                    val LANGUAGE : String =  "vi"
                    gia = document.data?.getValue("gia").toString()
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(gia.toInt())

                    giasp.setText(numberFormat.toString())
                    tensanpham.setText(document.data?.getValue("ten").toString())
                    mota.setText(document.data?.getValue("mota").toString())
                    linkanh = document.data?.getValue("linkanh").toString()
                    Picasso.get().load(document.data?.getValue("linkanh").toString()).placeholder(R.drawable.banner_home).error(R.drawable.banner_home).into(imageView)
                }
            }



        view.findViewById<Button>(R.id.tang).setOnClickListener {
            val txtsoluong = soluong
            var tong : Int = txtsoluong.text.toString().toInt().plus(1)
            txtsoluong.setText(tong.toString())
        }
        view.findViewById<Button>(R.id.giam).setOnClickListener {
            val txtsoluong = soluong
            if (txtsoluong.text.toString() != "0"){
                var tong : Int = txtsoluong.text.toString().toInt().minus(1)
                txtsoluong.setText(tong.toString())
            }
        }


        view.findViewById<Button>(R.id.addcart).setOnClickListener {

            var tongsoluong = soluong.text.toString()

            //current time
            val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
            val currentDate = sdf.format(Date())


            val user = Firebase.auth.currentUser
            val uid = user?.uid
            val sodienthoai : Int = 84
            database = FirebaseDatabase.getInstance().getReference("cart")
            val length = 20

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val randomid = formatter.format(now).toString()

            val randomString = randomid.plus(uid)
            val data = mapOf<String, Any>(
                "gia" to gia.toInt(),
                "ten" to tensp.text,
                "mota" to motasp.text,
                "id" to uid!!,
                "soluong" to tongsoluong.toInt(),
                "tinhtrang" to "Cart",
                "linkanh" to linkanh,
                "idsanpham" to id,
                "idcart" to randomString,
                "diachi" to "",
                "sodienthoai" to sodienthoai,
                "ngaydathang" to currentDate,
                "ngaygiaohang" to "",
                "ngaynhanhang" to ""
            )
            database.child(randomString).updateChildren(data).addOnCompleteListener {
                Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
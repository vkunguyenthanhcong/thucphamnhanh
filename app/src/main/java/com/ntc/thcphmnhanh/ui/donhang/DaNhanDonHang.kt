package com.ntc.thcphmnhanh.ui.donhang


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_da_nhan_don_hang.*
import org.w3c.dom.Text
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class DaNhanDonHang : Fragment() {
    private val args : DaNhanDonHangArgs by navArgs()
    private lateinit var dbref : DatabaseReference
    private lateinit var alertDialogBuilder : AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_da_nhan_don_hang, container, false)
        val idcart = args.idcart
        alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle("Xác nhận hủy đơn")
        alertDialogBuilder.setIcon(R.drawable.ic_delete)
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn hủy đơn?")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Đồng ý") { dialog: DialogInterface, which: Int ->

            dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
            val data = mapOf<String, Any>(
                "id" to "",
                "tinhtrang" to "Đã hủy đơn"
            )
            dbref.updateChildren(data).addOnCompleteListener {
                Toast.makeText(context, "Hủy đơn thành công", Toast.LENGTH_SHORT).show()
                findNavController().navigate(DaNhanDonHangDirections.actionNavThongtindonhangToNavDonhang())
            }
        }
        alertDialogBuilder.setNegativeButton("Hủy") { dialog: DialogInterface, which: Int ->

        }


        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //image
                    Picasso.get().load(snapshot.child("linkanh").value.toString()).placeholder(com.ntc.thcphmnhanh.R.drawable.logo).into(view.findViewById<ImageView>(R.id.imagesp))
                    //Tên sản phẩm
                    tensp.setText(snapshot.child("ten").value.toString())
                    //Tình trạng sản phẩm
                    val tt = snapshot.child("tinhtrang").value.toString()
                    if (tt == "Cart"){
                        tinhtrang.setText("Đơn hàng mới")
                    }else if (tt == "Đã đặt hàng"){
                        tinhtrang.setText("Đã đặt hàng")
                        tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }else if (tt == "Đã gửi hàng"){
                        tinhtrang.setText("Đã gửi hàng")
                        tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }else if (tt == "Đã nhận hàng" || tt == "Đã hủy đơn"){
                        mainxacnhan.visibility = View.GONE
                        xoasp.visibility = View.GONE
                        tinhtrang.setText(tt)
                    }
                    //Giá sản phẩm
                    val COUNTRY : String = "VN"
                    val LANGUAGE : String =  "vi"
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("gia").value)
                    giasp.setText(numberFormat.toString())
                    //Số lượng và tổng tiền
                    soluong.setText(snapshot.child("soluong").value.toString())

                    val tongmoney = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("tongtien").value)
                    view.findViewById<TextView>(R.id.tongtien).setText(tongmoney.toString())
                    //thời gian đặt hàng và giao hàng
                    tgdathang.setText(snapshot.child("ngaydathang").value.toString())
                    tgguihang.setText(snapshot.child("ngaygiaohang").value.toString())
                    //địa chỉ và số điện thoại
                    diachi.setText(snapshot.child("diachi").value.toString())
                    phone.setText(snapshot.child("sodienthoai").value.toString())
                }
                xacnhan.setOnClickListener {
                    val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
                    val currentDate = sdf.format(Date())
                    dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
                    val data = mapOf<String, Any>(
                        "tinhtrang" to "Đã nhận hàng",
                        "ngaynhanhang" to currentDate
                    )
                    dbref.updateChildren(data).addOnCompleteListener {
                        val mediaPlayer = MediaPlayer.create(context, R.raw.ting)
                        mediaPlayer.start()
                        Toast.makeText(context, " Xác nhận thành công", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(DaNhanDonHangDirections.actionNavThongtindonhangToNavDonhang())
                    }
                }
                xoasp.setOnClickListener {
                    alertDialogBuilder.show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return view

    }

}
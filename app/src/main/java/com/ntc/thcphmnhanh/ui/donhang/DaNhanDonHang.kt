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
        alertDialogBuilder.setTitle("X??c nh???n h???y ????n")
        alertDialogBuilder.setIcon(R.drawable.ic_delete)
        alertDialogBuilder.setMessage("B???n c?? ch???c ch???n mu???n h???y ????n?")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("?????ng ??") { dialog: DialogInterface, which: Int ->

            dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
            val data = mapOf<String, Any>(
                "id" to "",
                "tinhtrang" to "???? h???y ????n"
            )
            dbref.updateChildren(data).addOnCompleteListener {
                Toast.makeText(context, "H???y ????n th??nh c??ng", Toast.LENGTH_SHORT).show()
                findNavController().navigate(DaNhanDonHangDirections.actionNavThongtindonhangToNavDonhang())
            }
        }
        alertDialogBuilder.setNegativeButton("H???y") { dialog: DialogInterface, which: Int ->

        }


        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //image
                    Picasso.get().load(snapshot.child("linkanh").value.toString()).placeholder(com.ntc.thcphmnhanh.R.drawable.logo).into(view.findViewById<ImageView>(R.id.imagesp))
                    //T??n s???n ph???m
                    tensp.setText(snapshot.child("ten").value.toString())
                    //T??nh tr???ng s???n ph???m
                    val tt = snapshot.child("tinhtrang").value.toString()
                    if (tt == "Cart"){
                        tinhtrang.setText("????n h??ng m???i")
                    }else if (tt == "???? ?????t h??ng"){
                        tinhtrang.setText("???? ?????t h??ng")
                        tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }else if (tt == "???? g???i h??ng"){
                        tinhtrang.setText("???? g???i h??ng")
                        tinhtrang.setTextColor(Color.parseColor("#32cd32"))
                    }else if (tt == "???? nh???n h??ng" || tt == "???? h???y ????n"){
                        mainxacnhan.visibility = View.GONE
                        xoasp.visibility = View.GONE
                        tinhtrang.setText(tt)
                    }
                    //Gi?? s???n ph???m
                    val COUNTRY : String = "VN"
                    val LANGUAGE : String =  "vi"
                    val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("gia").value)
                    giasp.setText(numberFormat.toString())
                    //S??? l?????ng v?? t???ng ti???n
                    soluong.setText(snapshot.child("soluong").value.toString())
                    view.findViewById<TextView>(R.id.tongtien).setText(NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(snapshot.child("tongtien").value))
                    //th???i gian ?????t h??ng v?? giao h??ng
                    tgdathang.setText(snapshot.child("ngaydathang").value.toString())
                    tgguihang.setText(snapshot.child("ngaygiaohang").value.toString())
                    //?????a ch??? v?? s??? ??i???n tho???i
                    diachi.setText(snapshot.child("diachi").value.toString())
                    phone.setText(snapshot.child("sodienthoai").value.toString())
                }
                xacnhan.setOnClickListener {
                    val sdf = SimpleDateFormat("dd/MM/yyyy kk:mm:ss")
                    val currentDate = sdf.format(Date())
                    dbref = FirebaseDatabase.getInstance().getReference("cart").child(idcart)
                    val data = mapOf<String, Any>(
                        "tinhtrang" to "???? nh???n h??ng",
                        "ngaynhanhang" to currentDate
                    )
                    dbref.updateChildren(data).addOnCompleteListener {
                        val mediaPlayer = MediaPlayer.create(context, R.raw.ting)
                        mediaPlayer.start()
                        Toast.makeText(context, " X??c nh???n th??nh c??ng", Toast.LENGTH_SHORT).show()
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
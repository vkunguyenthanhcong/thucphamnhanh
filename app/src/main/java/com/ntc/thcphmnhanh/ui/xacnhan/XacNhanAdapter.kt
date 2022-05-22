package com.ntc.thcphmnhanh.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.AdminXacNhanDonHang
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.XacNhanDonHang
import com.ntc.thcphmnhanh.ui.admin.GioHang
import com.ntc.thcphmnhanh.ui.admin.XacNhan
import com.squareup.picasso.Picasso


class XacNhanAdapter(var cartList: ArrayList<XacNhan>, var clickListener: OnSPItemClickListener) : RecyclerView.Adapter<XacNhanAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewxacnhan,
            parent,false)


        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = cartList[position]
        holder.initialize(cartList.get(position), clickListener)

    }

    override fun getItemCount(): Int {

        return cartList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val ten : TextView = itemView.findViewById(R.id.tensp)
        val gia : TextView = itemView.findViewById(R.id.giasp)
        val soluong : TextView = itemView.findViewById(R.id.soluong)
        val tinhtrang : TextView = itemView.findViewById(R.id.tinhtrang)
        val image : ImageView = itemView.findViewById(R.id.imagesp)
        val xacnhan : Button = itemView.findViewById(R.id.xacnhan)
        val lienhe : Button = itemView.findViewById(R.id.lienhe)
        fun initialize(item: XacNhan, action: OnSPItemClickListener){
            ten.text = item.ten
            gia.text = item.gia.toString()
            soluong.text = item.soluong.toString()
            if (item.tinhtrang == "Đã đặt hàng"){
                tinhtrang.setTextColor(Color.parseColor("#ffff00"))
            }else if (item.tinhtrang == "Đã gửi hàng"){
                xacnhan.visibility = View.GONE
                tinhtrang.setText("Đang giao hàng")
                tinhtrang.setTextColor(Color.parseColor("#ffff00"))
            } else if (item.tinhtrang == "Đã nhận hàng"){
                tinhtrang.setTextColor(Color.parseColor("#00ff00"))

            }else{
                tinhtrang.setTextColor(Color.parseColor("#ff0000"))
            }
            Picasso.get().load(item.linkanh).placeholder(R.drawable.logo).error(R.drawable.logo).into(image)
            xacnhan.setOnClickListener {
                val intent = Intent(itemView.context, AdminXacNhanDonHang::class.java)
                intent.putExtra("idcart", item.idcart)
                itemView.context.startActivity(intent)
            }
            lienhe.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + "${item.sodienthoai}")
                itemView.context.startActivity(dialIntent)
            }
        }
    }

    interface OnSPItemClickListener{
        fun onItemClick(item : XacNhan, position: Int)
    }



}
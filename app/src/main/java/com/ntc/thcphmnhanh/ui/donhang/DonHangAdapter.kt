package com.ntc.thcphmnhanh.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.ui.admin.DonHang
import com.ntc.thcphmnhanh.ui.admin.GioHang
import com.ntc.thcphmnhanh.ui.donhang.DonHangFragment
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class DonHangAdapter(var cartList: ArrayList<DonHang>, var clickListener: DonHangAdapter.OnSPItemClickListener) : RecyclerView.Adapter<DonHangAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewdonhang,
            parent,false)
             return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem : DonHang = cartList[position]
        holder.initialize(cartList.get(position), clickListener)

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tensp : TextView = itemView.findViewById(R.id.tensp)
        val giasp : TextView = itemView.findViewById(R.id.giasp)
        val soluong : TextView = itemView.findViewById(R.id.soluong)
        val tinhtrang : TextView = itemView.findViewById(R.id.tinhtrang)
        val image : ImageView = itemView.findViewById(R.id.imagesp)
        val xacnhan : Button = itemView.findViewById(R.id.xacnhan)
        val lienhe = itemView.findViewById<Button>(R.id.lienhe)
        fun initialize(item: DonHang, action: DonHangAdapter.OnSPItemClickListener){
            tensp.text = item.ten
            val COUNTRY : String = "VN"
            val LANGUAGE : String =  "vi"
            val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(item.gia)
            giasp.text = numberFormat.toString()
            soluong.text = item.soluong.toString()
            tinhtrang.text = item.tinhtrang
            if (item.tinhtrang == "Đã đặt hàng"){
                tinhtrang.setTextColor(Color.parseColor("#ffff00"))
            }else if (item.tinhtrang == "Đang giao hàng"){
                tinhtrang.setTextColor(Color.parseColor("#ffff00"))
            } else if (item.tinhtrang == "Đã nhận hàng"){
                tinhtrang.setTextColor(Color.parseColor("#00ff00"))
            }
            Picasso.get().load(item.linkanh).placeholder(R.drawable.logo).error(R.drawable.logo).into(image)
            xacnhan.setOnClickListener {
                action.onItemClick(itemView,item, adapterPosition)
            }
            lienhe.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + "${item.sodienthoai}")
                itemView.context.startActivity(dialIntent)
            }

        }
    }
    interface OnSPItemClickListener{
        fun onItemClick(itemView: View, item : DonHang, position: Int)

    }
}
package com.ntc.thcphmnhanh.home

import android.content.Context
import android.content.Intent
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
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.XacNhanDonHang
import com.ntc.thcphmnhanh.ui.admin.GioHang
import com.squareup.picasso.Picasso


class GioHangAdapter(var cartList: ArrayList<GioHang>, var clickListener: OnSPItemClickListener) : RecyclerView.Adapter<GioHangAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewcart,
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

        val tensp : TextView = itemView.findViewById(R.id.tensp)
        val giasp : TextView = itemView.findViewById(R.id.giasp)
        val image : ImageView = itemView.findViewById(R.id.imagesp)
        val soluong : EditText = itemView.findViewById(R.id.soluong)
        val buy : Button = itemView.findViewById(R.id.buy)
        val tang : Button = itemView.findViewById(R.id.tang)
        val giam : Button = itemView.findViewById(R.id.giam)
        var idcart: Any ?= null
        val dbref = FirebaseDatabase.getInstance().getReference("cart")
        fun initialize(item: GioHang, action: OnSPItemClickListener){
            tensp.text = item.ten
            giasp.text = item.gia.toString()
            soluong.setText(item.soluong.toString())
            idcart = item.idcart
            Picasso.get().load(item.linkanh).placeholder(R.drawable.logo).error(R.drawable.logo).into(image)

            tang.setOnClickListener {
                val tongso : Int = soluong.text.toString().toInt().plus(1)
                soluong.setText(tongso.toString())
                val data = mapOf<String, Any>(
                    "$idcart/soluong" to tongso
                )
                dbref.updateChildren(data)
            }
            giam.setOnClickListener {
                val tongso : Int = soluong.text.toString().toInt().minus(1)
                soluong.setText(tongso.toString())
                val data = mapOf<String, Any>(
                    "$idcart/soluong" to tongso
                )
                dbref.updateChildren(data)
            }

            buy.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    interface OnSPItemClickListener{
        fun onItemClick(item : GioHang, position: Int)
    }



}
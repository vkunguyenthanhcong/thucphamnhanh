package com.ntc.thcphmnhanh.ui.xacnhan

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.ui.admin.XacNhan
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


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
            val COUNTRY : String = "VN"
            val LANGUAGE : String =  "vi"
            val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(item.gia)
            gia.text = numberFormat.toString()
            soluong.text = item.soluong.toString()
            tinhtrang.setText(item.tinhtrang)
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
        fun onItemClick(itemView: View, item : XacNhan, position: Int)
    }



}
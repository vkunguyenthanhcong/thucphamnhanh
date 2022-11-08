package com.ntc.thcphmnhanh.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class ProductAdapter(var cartList: ArrayList<ProductData>, var clickListener: OnSPItemClickListener) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewlistadmin,
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

        val ten : TextView = itemView.findViewById(R.id.title)
        val email : TextView = itemView.findViewById(R.id.describe)
        val image : ImageView = itemView.findViewById(R.id.image)

        fun initialize(item: ProductData, action: OnSPItemClickListener){
            ten.text = item.ten
            email.text = item.gia

            Picasso.get().load(item.linkanh).placeholder(R.drawable.logo).error(R.drawable.logo).into(image)
            itemView.findViewById<ImageButton>(R.id.btnview).setOnClickListener {
                action.onItemClick(itemView,item, adapterPosition)
            }

        }
    }

    interface OnSPItemClickListener{
        fun onItemClick(itemView: View, item : ProductData, position: Int)
    }



}
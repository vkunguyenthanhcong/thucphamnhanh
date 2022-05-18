package com.ntc.thcphmnhanh.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.FastFood
import com.ntc.thcphmnhanh.ui.gallery.nThucPham
import com.squareup.picasso.Picasso

class nThucPhamAdapter(private val TPList: ArrayList<nThucPham>) : RecyclerView.Adapter<nThucPhamAdapter.MyViewHolder>(){

    private lateinit var TPListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        TPListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): nThucPhamAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewthucpham, parent, false)
        return MyViewHolder(itemView, TPListener)
    }

    override fun onBindViewHolder(holder: nThucPhamAdapter.MyViewHolder, position: Int) {
        val menu : nThucPham = TPList[position]
        holder.text.text = menu.ten
        var imageurl = menu.linkanh
        var imageview = holder.image

        Picasso.get().load(imageurl).placeholder(R.drawable.loading).error(R.drawable.loading).fit().into(imageview)
    }

    override fun getItemCount(): Int {
        return TPList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.imagef)
        val text : TextView = itemView.findViewById(R.id.textf)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
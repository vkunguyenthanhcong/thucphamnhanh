package com.ntc.thcphmnhanh.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.ThucPham
import com.ntc.thcphmnhanh.ui.admin.SanPham
import com.squareup.picasso.Picasso

class SanPhamAdapter(private val menuList: ArrayList<SanPham>) : RecyclerView.Adapter<SanPhamAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SanPhamAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewuser, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: SanPhamAdapter.MyViewHolder, position: Int) {
        val menu : SanPham = menuList[position]
        holder.text1.text = menu.ten
        holder.text2.text = menu.gia
        var imageurl = menu.linkanh
        var imageview = holder.image

        Picasso.get().load(imageurl).placeholder(R.drawable.loading).error(R.drawable.loading).fit().into(imageview)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.imageu)
        val text1 : TextView = itemView.findViewById(R.id.nameuser)
        val text2 : TextView = itemView.findViewById(R.id.emailuser)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
package com.ntc.thcphmnhanh.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.ThucPham
import com.squareup.picasso.Picasso

class ThucPhamAdapter(private val menuList: ArrayList<ThucPham>) : RecyclerView.Adapter<ThucPhamAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThucPhamAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewdanhsach, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ThucPhamAdapter.MyViewHolder, position: Int) {
        val menu : ThucPham = menuList[position]
        holder.text.text = menu.ten
        holder.text2.text = menu.gia
        holder.text3.text = menu.mota
        var imageurl = menu.linkanh
        var imageview = holder.image

        Picasso.get().load(imageurl).placeholder(R.drawable.loading).error(R.drawable.loading).fit().into(imageview)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.imagesp)
        val text : TextView = itemView.findViewById(R.id.tensp)
        val text2 : TextView = itemView.findViewById(R.id.giasp)
        val text3 : TextView = itemView.findViewById(R.id.motasp)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
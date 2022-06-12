package com.ntc.thcphmnhanh.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ThucPhamAdapter(private val menuList: ArrayList<com.ntc.thcphmnhanh.ui.home.ThucPham>) : RecyclerView.Adapter<ThucPhamAdapter.MyViewHolder>(){

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
        val menu : com.ntc.thcphmnhanh.ui.home.ThucPham = menuList[position]
        holder.text.text = menu.ten

        holder.text3.text = menu.mota
        var imageurl = menu.linkanh
        var imageview = holder.image
        val COUNTRY : String = "VN"
        val LANGUAGE : String =  "vi"
        val gia = menu.gia
        val numberFormat = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(gia.toString().toInt())
        holder.text2.text = numberFormat
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
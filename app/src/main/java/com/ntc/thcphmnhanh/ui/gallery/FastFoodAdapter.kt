package com.ntc.thcphmnhanh.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.FastFood
import com.squareup.picasso.Picasso

class FastFoodAdapter(private val FFList: ArrayList<FastFood>) : RecyclerView.Adapter<FastFoodAdapter.MyViewHolder>(){

    private lateinit var FFListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        FFListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastFoodAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewthucannhanh, parent, false)
        return MyViewHolder(itemView, FFListener)
    }

    override fun onBindViewHolder(holder: FastFoodAdapter.MyViewHolder, position: Int) {
        val menu : FastFood = FFList[position]
        holder.text.text = menu.ten
        var imageurl = menu.linkanh
        var imageview = holder.image

        Picasso.get().load(imageurl).placeholder(R.drawable.loading).error(R.drawable.loading).fit().into(imageview)
    }

    override fun getItemCount(): Int {
        return FFList.size
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
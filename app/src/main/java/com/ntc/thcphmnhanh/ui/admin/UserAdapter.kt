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
import com.ntc.thcphmnhanh.ui.admin.User
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycleviewuser, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val data : User = userList[position]
        holder.text1.text = data.hoten
        holder.text2.text = data.email
        Picasso.get().load(data.link).placeholder(R.drawable.user).error(R.drawable.user).fit().into(holder.anh)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val anh : ImageView = itemView.findViewById(R.id.imageu)
        val text1 : TextView = itemView.findViewById(R.id.nameuser)
        val text2 : TextView = itemView.findViewById(R.id.emailuser)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
package com.ntc.thcphmnhanh.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.ThucPham
import com.ntc.thcphmnhanh.ui.admin.UserData
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.MyViewHolder>(){

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
        val user : UserData = userList[position]
        holder.nameuser.text = user.hoten
        holder.email.text = user.email
        var imageurl = user.link
        var imageview = holder.image

        Picasso.get().load(imageurl).placeholder(R.drawable.loading).error(R.drawable.loading).fit().into(imageview)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.imageuser)
        val nameuser : TextView = itemView.findViewById(R.id.nameuser)
        val email : TextView = itemView.findViewById(R.id.emailuser)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
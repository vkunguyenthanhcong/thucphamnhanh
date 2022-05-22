package com.ntc.thcphmnhanh.ui.donhang

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.databinding.FragmentDonHangBinding
import com.ntc.thcphmnhanh.home.DonHangAdapter
import com.ntc.thcphmnhanh.ui.admin.DonHang

class DonHangFragment() : Fragment(), DonHangAdapter.OnSPItemClickListener{
    private var _binding: FragmentDonHangBinding? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartArrayList : ArrayList<DonHang>
    private lateinit var cartAdapter: DonHangAdapter
    private lateinit var adapter : Any
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonHangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.donhang
        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        cartArrayList = arrayListOf<DonHang>()
        cartAdapter = DonHangAdapter(cartArrayList, this)
        recyclerView.adapter = DonHangAdapter(cartArrayList, this)
        adapter = recyclerView.adapter as DonHangAdapter
        EventChangeListener()

        return root
    }
    private fun EventChangeListener(){
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        dbref =
            FirebaseDatabase.getInstance().getReference().child("cart")
        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                recyclerView.visibility = View.GONE
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(DonHang::class.java)
                        if (user != null) {
                            if (user.id == uid){
                                cartArrayList.add(user!!)
                            }
                        }

                    }
                    adapter
                    recyclerView.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
    override fun onItemClick(itemView: View,item: DonHang, position: Int) {

        dbref = FirebaseDatabase.getInstance().getReference("cart").child(item.idcart.toString())
        val data = mapOf<String, Any>(
            "tinhtrang" to "Đã nhận hàng"
        )
        dbref.updateChildren(data).addOnCompleteListener {
            val mediaPlayer = MediaPlayer.create(mContext, R.raw.ting)
            mediaPlayer.start()
            Toast.makeText(mContext, "Xác nhận thành công", Toast.LENGTH_SHORT).show()
        }
    }
}



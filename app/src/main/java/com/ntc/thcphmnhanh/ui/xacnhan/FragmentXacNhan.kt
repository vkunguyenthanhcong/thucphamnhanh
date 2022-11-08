package com.ntc.thcphmnhanh.ui.xacnhan

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.databinding.FragmentSlideshowBinding
import com.ntc.thcphmnhanh.databinding.FragmentXacNhanBinding
import com.ntc.thcphmnhanh.home.DonHangAdapter
import com.ntc.thcphmnhanh.ui.admin.DonHang
import com.ntc.thcphmnhanh.ui.admin.XacNhan

class FragmentXacNhan : Fragment(), XacNhanAdapter.OnSPItemClickListener {
    private var _binding: FragmentXacNhanBinding? = null
    private var mContext: Context? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartArrayList : ArrayList<XacNhan>
    private lateinit var cartAdapter: XacNhanAdapter
    private lateinit var adapter : Any
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
        _binding = FragmentXacNhanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.xacnhan
        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        cartArrayList = arrayListOf<XacNhan>()
        cartAdapter = XacNhanAdapter(cartArrayList, this)
        recyclerView.adapter = XacNhanAdapter(cartArrayList, this)
        adapter = recyclerView.adapter as XacNhanAdapter
        EventChangeListener()

        return root
    }
    private fun EventChangeListener(){

        dbref =
            FirebaseDatabase.getInstance().getReference().child("cart")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                recyclerView.visibility = View.GONE
                cartArrayList.clear()
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(XacNhan::class.java)
                        if (user != null) {
                                cartArrayList.add(user!!)
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
    override fun onItemClick(itemView: View, item: XacNhan, position: Int) {
            val idcart = item.idcart
            findNavController().navigate(FragmentXacNhanDirections.actionNavXacnhanToNavVerify(idcart.toString()))
    }
}
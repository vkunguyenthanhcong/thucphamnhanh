package com.ntc.thcphmnhanh.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ntc.thcphmnhanh.XacNhanDonHang
import com.ntc.thcphmnhanh.databinding.FragmentGioHangBinding
import com.ntc.thcphmnhanh.home.GioHangAdapter
import com.ntc.thcphmnhanh.ui.admin.GioHang


class GioHangFragment : Fragment() , GioHangAdapter.OnSPItemClickListener{
    private var _binding: FragmentGioHangBinding? = null
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartArrayList : ArrayList<GioHang>
    private lateinit var cartAdapter: GioHangAdapter
    private lateinit var adapter : Any


    private val binding get() = _binding!!
        private var mContext: Context? = null
        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGioHangBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.giohang

        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        cartArrayList = arrayListOf<GioHang>()
        cartAdapter = GioHangAdapter(cartArrayList, this)
        recyclerView.adapter = GioHangAdapter(cartArrayList, this)
        adapter = recyclerView.adapter as GioHangAdapter
        EventChangeListener()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun EventChangeListener(){
        dbref =
            FirebaseDatabase.getInstance().getReference().child("cart")
        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                recyclerView.visibility = View.GONE
                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(GioHang::class.java)
                        if (user != null) {
                            if (user.tinhtrang == "Cart"){
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

    override fun onItemClick(item: GioHang, position: Int) {
        val intent = Intent(mContext, XacNhanDonHang::class.java)
        intent.putExtra("idcart", item.idcart)
        startActivity(intent)
    }

}
package com.ntc.thcphmnhanh.ui.donhang

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.home.DonHangAdapter
import com.ntc.thcphmnhanh.ui.admin.DonHang


class DonHangFragment() : Fragment(), DonHangAdapter.OnSPItemClickListener{

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_don_hang, container, false)

        recyclerView = view.findViewById(R.id.donhang)



        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        cartArrayList = arrayListOf<DonHang>()
        cartAdapter = DonHangAdapter(cartArrayList, this)
        recyclerView.adapter = DonHangAdapter(cartArrayList, this)
        adapter = recyclerView.adapter as DonHangAdapter
        EventChangeListener()

        return view
    }

    private fun EventChangeListener(){
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        dbref = FirebaseDatabase.getInstance().getReference().child("cart")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cartArrayList.clear()
                recyclerView.visibility = View.GONE
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(DonHang::class.java)
                        if (user != null) {

                                if (user.tinhtrang != "Cart" && user.id == uid){
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
            val idcart = item.idcart
            findNavController().navigate(DonHangFragmentDirections.actionNavDonhangToNavThongtindonhang(idcart.toString()))
    }
}



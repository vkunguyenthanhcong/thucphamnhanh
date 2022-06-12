package com.ntc.thcphmnhanh.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.DetailProductArgs

class ListProduct : Fragment() {
    private val args : ListProductArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuArrayList: ArrayList<ThucPham>
    private lateinit var menuAdapter: ThucPhamAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var dbname : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_product, container, false)

        dbname = args.id

        recyclerView = view.findViewById(R.id.recycleviewdanhsach)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.setHasFixedSize(true)

        menuArrayList = arrayListOf()
        menuAdapter = ThucPhamAdapter(menuArrayList)

        recyclerView.adapter = menuAdapter

        EventChangeListener()
        menuAdapter.setOnItemClickListener(object : ThucPhamAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                findNavController().navigate(ListProductDirections.actionNavListToNavDetail(menuArrayList[position].id.toString()))
            }

        })

        return view
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").whereEqualTo("loai", dbname).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        menuArrayList.add(dc.document.toObject(ThucPham::class.java))
                    }
                }
                menuAdapter.notifyDataSetChanged()
            }

        })
    }
}
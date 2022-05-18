package com.ntc.thcphmnhanh.thucpham

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.home.ThucPhamAdapter

class ListThucPham : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var menuArrayList: ArrayList<ThucPham>
    private lateinit var menuAdapter: ThucPhamAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var dbname : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_thuc_pham)

        dbname = intent.getStringExtra("id").toString()
        recyclerView = findViewById(R.id.recycleviewdanhsach)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.setHasFixedSize(true)

        menuArrayList = arrayListOf()
        menuAdapter = ThucPhamAdapter(menuArrayList)

        recyclerView.adapter = menuAdapter

        EventChangeListener()
        menuAdapter.setOnItemClickListener(object : ThucPhamAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@ListThucPham, ChiTietSanPham::class.java)
                intent.putExtra("id", menuArrayList[position].id)
                startActivity(intent)
            }

        })
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").whereEqualTo("loai", dbname).addSnapshotListener(object : EventListener<QuerySnapshot> {
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
package com.ntc.thcphmnhanh.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.home.MenuAdapter
import com.ntc.thcphmnhanh.home.MenuData
import com.ntc.thcphmnhanh.ui.donhang.DaNhanDonHangArgs
import com.ntc.thcphmnhanh.ui.xacnhan.FragmentXacNhanDirections
import com.ntc.thcphmnhanh.ui.xacnhan.XacNhanAdapter
import kotlinx.android.synthetic.main.fragment_admin_list.*

class AdminList : Fragment(), UserAdapter.OnSPItemClickListener, ProductAdapter.OnSPItemClickListener {
    private val args : AdminListArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var adapter : Any

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_list, container, false)

        recyclerView = view.findViewById(R.id.listuser)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        if (args.id == "user"){
            listuser()
        }else if (args.id == "product"){
            listproduct()
        }

        return view
    }
    protected fun listuser(){
        var menuArrayList : ArrayList<UserData>
        var menuAdapter: UserAdapter
        menuArrayList = arrayListOf()
        menuAdapter = UserAdapter(menuArrayList, this)
        recyclerView.adapter = menuAdapter

        db = FirebaseFirestore.getInstance()
        db.collection("users").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        menuArrayList.add(dc.document.toObject(UserData::class.java))
                    }
                }
                menuAdapter.notifyDataSetChanged()
            }

        })
    }
    protected fun listproduct(){
        var menuArrayList : ArrayList<ProductData>
        var menuAdapter: ProductAdapter
        menuArrayList = arrayListOf()
        menuAdapter = ProductAdapter(menuArrayList, this)
        recyclerView.adapter = menuAdapter

        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        menuArrayList.add(dc.document.toObject(ProductData::class.java))
                    }
                }
                menuAdapter.notifyDataSetChanged()
            }

        })
    }
    override fun onItemClick(itemView: View, item: UserData, position: Int) {

        findNavController().navigate(AdminListDirections.actionNavListAdminToNavDetailUser(item.id.toString()))

    }

    override fun onItemClick(itemView: View, item: ProductData, position: Int) {

        findNavController().navigate(AdminListDirections.actionNavListAdminToNavDetailProduct(item.id.toString()))

    }
}
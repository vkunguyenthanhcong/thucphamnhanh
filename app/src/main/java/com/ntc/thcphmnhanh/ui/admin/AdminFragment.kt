package com.ntc.thcphmnhanh.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.databinding.FragmentAdminBinding
import com.ntc.thcphmnhanh.databinding.FragmentHomeBinding
import com.ntc.thcphmnhanh.home.*
import com.ntc.thcphmnhanh.thucpham.ChiTietSanPham
import com.ntc.thcphmnhanh.thucpham.FastFood
import com.ntc.thcphmnhanh.thucpham.ListThucPham
import com.ntc.thcphmnhanh.thucpham.ThucPham
import com.ntc.thcphmnhanh.ui.gallery.nThucPham
import com.squareup.picasso.Picasso

class AdminFragment : Fragment() {
    private var _binding: FragmentAdminBinding? = null

    //user
    private lateinit var recyclerview: RecyclerView
    //sản phẩm
    private lateinit var nrecyclerView: RecyclerView
    private lateinit var spArrayList: ArrayList<SanPham>
    private lateinit var spAdapter: SanPhamAdapter

    private lateinit var db : FirebaseFirestore
    // This property is only valid between onCreateView and
    // onDestroyView.
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
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = Firebase.auth.currentUser
        val uid = user?.uid
        sanphamshow()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sanphamshow(){
        nrecyclerView = binding.sp

        val nlinearLayoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        nrecyclerView.layoutManager = nlinearLayoutManager

        spArrayList = arrayListOf()
        spAdapter = SanPhamAdapter(spArrayList)

        nrecyclerView.adapter = spAdapter

        nEventChangeListener()
        spAdapter.setOnItemClickListener(object : SanPhamAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("id", spArrayList[position].id)
                startActivity(intent)
            }

        })
    }
    private fun nEventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        spArrayList.add(dc.document.toObject(SanPham::class.java))
                    }
                }
                spAdapter.notifyDataSetChanged()
            }

        })
    }

}
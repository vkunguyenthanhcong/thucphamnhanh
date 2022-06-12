package com.ntc.thcphmnhanh.ui.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.ntc.thcphmnhanh.databinding.FragmentGalleryBinding
import com.ntc.thcphmnhanh.home.FastFoodAdapter
import com.ntc.thcphmnhanh.home.nThucPhamAdapter
import com.ntc.thcphmnhanh.thucpham.FastFood

class GalleryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var FFArrayList: ArrayList<FastFood>
    private lateinit var FFAdapter: FastFoodAdapter

    private lateinit var nrecyclerView: RecyclerView
    private lateinit var TPArrayList: ArrayList<nThucPham>
    private lateinit var TPAdapter: nThucPhamAdapter

    private lateinit var db : FirebaseFirestore
    private var _binding: FragmentGalleryBinding? = null

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
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //menu list thuc pham
        nrecyclerView = binding.thucpham

        val nlinearLayoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        nrecyclerView.layoutManager = nlinearLayoutManager

        TPArrayList = arrayListOf()
        TPAdapter = nThucPhamAdapter(TPArrayList)

        nrecyclerView.adapter = TPAdapter

        nEventChangeListener()
        TPAdapter.setOnItemClickListener(object : nThucPhamAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                findNavController().navigate(GalleryFragmentDirections.actionNavGalleryToNavDetail(TPArrayList[position].id.toString()))

            }

        })

        //menu list thuc an nhanh
        recyclerView = binding.thucannhanh

        val linearLayoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

        FFArrayList = arrayListOf()
        FFAdapter = FastFoodAdapter(FFArrayList)

        recyclerView.adapter = FFAdapter

        EventChangeListener()
        FFAdapter.setOnItemClickListener(object : FastFoodAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
               findNavController().navigate(GalleryFragmentDirections.actionNavGalleryToNavDetail(TPArrayList[position].id.toString()))

            }

        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun nEventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").whereEqualTo("danhsach", "thucpham").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        TPArrayList.add(dc.document.toObject(nThucPham::class.java))
                    }
                }
                TPAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("danhsach").whereEqualTo("danhsach", "thucannhanh").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        FFArrayList.add(dc.document.toObject(FastFood::class.java))
                    }
                }
                FFAdapter.notifyDataSetChanged()
            }

        })
    }
}
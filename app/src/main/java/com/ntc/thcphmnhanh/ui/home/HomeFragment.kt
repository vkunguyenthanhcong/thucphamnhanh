package com.ntc.thcphmnhanh.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.databinding.FragmentHomeBinding
import com.ntc.thcphmnhanh.home.MenuAdapter
import com.ntc.thcphmnhanh.home.MenuData

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuArrayList: ArrayList<MenuData>
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var db : FirebaseFirestore
    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val banner: ImageView = binding.banner
        val user = Firebase.auth.currentUser
        val uid = user?.uid

        //menu list
        recyclerView = binding.recyclemenu
        recyclerView.layoutManager = GridLayoutManager(context, 6)
        recyclerView.setHasFixedSize(true)

        menuArrayList = arrayListOf()
        menuAdapter = MenuAdapter(menuArrayList)

        recyclerView.adapter = menuAdapter

        EventChangeListener()
        menuAdapter.setOnItemClickListener(object : MenuAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavList(menuArrayList[position].id.toString()))
            }

        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("menu").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        menuArrayList.add(dc.document.toObject(MenuData::class.java))
                    }
                }
                menuAdapter.notifyDataSetChanged()
            }

        })
    }



}
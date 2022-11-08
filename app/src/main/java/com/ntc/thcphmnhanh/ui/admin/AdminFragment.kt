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
import androidx.navigation.fragment.findNavController
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
import com.ntc.thcphmnhanh.thucpham.FastFood
import com.ntc.thcphmnhanh.ui.gallery.nThucPham
import com.squareup.picasso.Picasso

class AdminFragment : Fragment() {
    private var _binding: FragmentAdminBinding? = null


    private lateinit var db : FirebaseFirestore

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

        binding.btnuser.setOnClickListener {
            findNavController().navigate(AdminFragmentDirections.actionNavAdminToNavListAdmin("user"))
        }
        binding.btnsp.setOnClickListener {
            findNavController().navigate(AdminFragmentDirections.actionNavAdminToNavListAdmin("product"))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
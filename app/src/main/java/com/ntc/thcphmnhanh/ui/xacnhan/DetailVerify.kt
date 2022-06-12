package com.ntc.thcphmnhanh.ui.xacnhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ntc.thcphmnhanh.R
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailVerify : Fragment() {
    private lateinit var dbref : DatabaseReference
    private val args : DetailVerifyArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_verify, container, false)

        val idcart = args.id

        dbref = FirebaseDatabase.getInstance().getReference().child("cart").child(idcart)

        return view
    }

}
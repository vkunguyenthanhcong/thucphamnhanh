package com.ntc.thcphmnhanh.ui.admin

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.DetailProductArgs
import com.squareup.picasso.Picasso
import java.util.HashMap

class DetailUser : Fragment() {
    private val args : DetailUserArgs by navArgs()
    private lateinit var alertDialogBuilder : AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_user, container, false)

        alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle("Xác nhận xóa sản phẩm")
        alertDialogBuilder.setIcon(R.drawable.ic_delete)
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm?")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Xóa") { dialog: DialogInterface, which: Int ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(args.id).delete()
                .addOnCompleteListener {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(DetailProductDirections.actionNavDetailUserToNavListAdmin("user"))
                }
        }
        alertDialogBuilder.setNegativeButton("Hủy") { dialog: DialogInterface, which: Int ->

        }

        val imageView : ImageView = view.findViewById(R.id.avatar)
        val hoten : EditText = view.findViewById(R.id.hoten)
        val diachi : EditText = view.findViewById(R.id.diachi)
        val sodienthoai : EditText = view.findViewById(R.id.sodienthoai)
        val nam : CheckBox = view.findViewById(R.id.nam)
        val nu : CheckBox = view.findViewById(R.id.nu)
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(args.id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){

                    hoten.setText(document.data?.getValue("hoten").toString())
                    diachi.setText(document.data?.getValue("diachi").toString())
                    sodienthoai.setText(document.data?.getValue("sodienthoai").toString())
                    Picasso.get().load(document.data?.getValue("link").toString()).into(imageView)

                    if (document.data?.getValue("gioitinh").toString() == "Nam"){
                        nam.isChecked = true
                        nu.isChecked = false
                    }else if (document.data?.getValue("gioitinh").toString() == "Nữ"){
                        nam.isChecked = false
                        nu.isChecked = true
                    }
                }
            }

        return view
    }
}
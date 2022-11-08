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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.thucpham.DetailProductArgs
import com.ntc.thcphmnhanh.ui.donhang.DaNhanDonHangArgs
import com.ntc.thcphmnhanh.ui.donhang.DaNhanDonHangDirections
import com.squareup.picasso.Picasso
import java.util.HashMap

class DetailProduct : Fragment() {
    private val args : DetailProductArgs by navArgs()
    private lateinit var alertDialogBuilder : AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle("Xác nhận xóa sản phẩm")
        alertDialogBuilder.setIcon(R.drawable.ic_delete)
        alertDialogBuilder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm?")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Xóa") { dialog: DialogInterface, which: Int ->
            val db = FirebaseFirestore.getInstance()
            db.collection("danhsach").document(args.id).delete()
                .addOnCompleteListener {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(DetailProductDirections.actionNavDetailUserToNavListAdmin("product"))
                }
        }
        alertDialogBuilder.setNegativeButton("Hủy") { dialog: DialogInterface, which: Int ->

        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_product2, container, false)

        val txttensp = view.findViewById<TextView>(R.id.tensp)
        val edttensp = view.findViewById<EditText>(R.id.edttensp)
        val btntensp = view.findViewById<ImageButton>(R.id.btntensp)


        val txtgiasp = view.findViewById<TextView>(R.id.giasp)
        val edtgiasp = view.findViewById<EditText>(R.id.edtgiasp)
        val btngiasp = view.findViewById<ImageButton>(R.id.btngiasp)

        val txtmotasp = view.findViewById<TextView>(R.id.motasp)
        val edtmotasp = view.findViewById<EditText>(R.id.edtmotasp)
        val btnmotasp = view.findViewById<ImageButton>(R.id.btnmotasp)

        val imageView = view.findViewById<ImageView>(R.id.image)

        edttensp.visibility = View.GONE
        edtgiasp.visibility = View.GONE
        edtmotasp.visibility = View.GONE


        btntensp.setOnClickListener {
            if (edttensp.visibility == View.GONE){
                txttensp.visibility = View.GONE
                edttensp.visibility = View.VISIBLE
            }else if (edttensp.visibility == View.VISIBLE){
                txttensp.visibility = View.VISIBLE
                edttensp.visibility = View.GONE
                txttensp.setText(edttensp.text.toString())
            }
        }

        btngiasp.setOnClickListener {
            if (edtgiasp.visibility == View.GONE){
                txtgiasp.visibility = View.GONE
                edtgiasp.visibility = View.VISIBLE
            }else if (edtgiasp.visibility == View.VISIBLE){
                txtgiasp.visibility = View.VISIBLE
                edtgiasp.visibility = View.GONE
                txtgiasp.setText(edtgiasp.text.toString())
            }
        }

        btnmotasp.setOnClickListener {
            if (edtmotasp.visibility == View.GONE){
                txtmotasp.visibility = View.GONE
                edtmotasp.visibility = View.VISIBLE
            }else if (edtmotasp.visibility == View.VISIBLE){
                txtmotasp.visibility = View.VISIBLE
                edtmotasp.visibility = View.GONE
                txtmotasp.setText(edtmotasp.text.toString())
            }
        }

        val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("danhsach").document(args.id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    txttensp.setText(document.data?.getValue("ten").toString())
                    txtgiasp.setText(document.data?.getValue("gia").toString())
                    txtmotasp.setText(document.data?.getValue("mota").toString())

                    edttensp.setText(document.data?.getValue("ten").toString())
                    edtgiasp.setText(document.data?.getValue("gia").toString())
                    edtmotasp.setText(document.data?.getValue("mota").toString())

                    Picasso.get().load(document.data?.getValue("linkanh").toString()).into(imageView)
                }
            }



        view.findViewById<Button>(R.id.save).setOnClickListener{
            val data: MutableMap<String, Any> = HashMap()
            data["ten"] = txttensp.text.toString()
            data["gia"] = txtgiasp.text.toString()
            data["mota"] = txtmotasp.text.toString()

            db.collection("danhsach").document(args.id)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(context, "Thay đổi thành công", Toast.LENGTH_SHORT).show()
                }
        }
        view.findViewById<Button>(R.id.xoa).setOnClickListener {
            alertDialogBuilder.show()
        }

        return view
    }

}
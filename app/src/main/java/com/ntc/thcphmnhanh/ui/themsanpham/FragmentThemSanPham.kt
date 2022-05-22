package com.ntc.thcphmnhanh.ui.themsanpham

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.ntc.thcphmnhanh.databinding.FragmentThemSanPhamBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class FragmentThemSanPham : Fragment() {
    private var _binding: FragmentThemSanPhamBinding? = null
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var photoUrl : String ?= null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemSanPhamBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner = binding.loaisp
        binding.hinhanh.setOnClickListener {
            launchGallery()
        }
        val progressDialog = ProgressDialog(mContext)
        progressDialog.setTitle("Thực phẩm nhanh")
        progressDialog.setMessage("Đang tải lên hình ảnh, vui lòng đợi")
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(
            mContext!!, com.ntc.thcphmnhanh.R.array.loaisp_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
        binding.thucannhanh.setOnClickListener {
            binding.thucannhanh.isChecked = true
            binding.thucpham.isChecked = false
        }
        binding.thucpham.setOnClickListener {
            binding.thucannhanh.isChecked = false
            binding.thucpham.isChecked = true
        }
        binding.submit.setOnClickListener {
            progressDialog.show()
            val tensp = binding.tensp.text.toString()
            val giasp = binding.giasp.text.toString()
            val mota = binding.motasp.text.toString()
            val loaisp = binding.loaisp.selectedItem.toString()
            var loai : String ?= null
            var danhsach : String ?= null
            if (loaisp == "Cá"){
                loai = "ca"
            }else if (loaisp == "Thịt"){
                loai = "thit"
            }else if (loaisp == "Rau xanh"){
                loai = "rauxanh"
            }else{
                loai = "fastfood"
            }
            if (binding.thucpham.isChecked()){
                danhsach = "thucpham"

            }else if(binding.thucannhanh.isChecked()){
                danhsach = "thucannhanh"

            }
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val randomid = formatter.format(now).toString()

            val db = FirebaseFirestore.getInstance()

            val data: MutableMap<String, Any> = HashMap()
            data["ten"] = tensp!!
            data["gia"] = giasp!!
            data["linkanh"] = ""
            data["mota"] = mota!!
            data["id"] = randomid!!
            data["loai"] = loai
            data["danhsach"] = danhsach!!
            uploadImage(randomid, progressDialog)
            db.collection("danhsach").document(randomid)
                .set(data)
                .addOnSuccessListener {

                    Toast.makeText(mContext, "Tải thông tin thành công", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(mContext, "Thất bại", Toast.LENGTH_SHORT).show()
                }
        }
        return root
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            imageUri = data?.data
            binding.hinhanh.setImageURI(imageUri)
        }
    }
    fun getValues(view: View) {
        Toast.makeText(mContext, "Spinner 1 " + binding.loaisp.selectedItem.toString(), Toast.LENGTH_LONG).show()
    }

    private fun uploadImage(randomid : String, progressDialog: ProgressDialog){

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("myImages/$fileName")
        storageReference.putFile(imageUri!!).continueWithTask{ task ->
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            var downloadUri = task.result

            val db = FirebaseFirestore.getInstance()

            val data: MutableMap<String, Any> = HashMap()
            data["linkanh"] = downloadUri.toString()
            db.collection("danhsach").document(randomid)
                .set(data, SetOptions.merge())
                .addOnCompleteListener {
                    Toast.makeText(mContext, "Tải ảnh thành công", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

        }
    }
}
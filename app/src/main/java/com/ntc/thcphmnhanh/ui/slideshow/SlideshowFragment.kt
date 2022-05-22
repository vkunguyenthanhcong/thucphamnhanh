package com.ntc.thcphmnhanh.ui.slideshow

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ntc.thcphmnhanh.MainActivity
import com.ntc.thcphmnhanh.R
import com.ntc.thcphmnhanh.databinding.FragmentSlideshowBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*

class SlideshowFragment : Fragment() {
    private val pickImage = 100
    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val imageView: CircleImageView ? = null
    lateinit var nam: CheckBox
    lateinit var nu: CheckBox
    private var _binding: FragmentSlideshowBinding? = null
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = Firebase.auth.currentUser

        val photoUrl = user?.photoUrl
        val uid = user?.uid

        val imageView: CircleImageView = binding.avatar
        Picasso.get().load(photoUrl).placeholder(R.drawable.user).error(R.drawable.user).into(imageView)

        imageView.setOnClickListener {
            launchGallery()
        }
        val button2: Button = binding.capnhatavatar
        button2.setOnClickListener {
            uploadImage()
        }

        readFirestoreData(uid)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val button: Button = binding.capnhatthongtin
        button.setOnClickListener {
            val hoten = binding.hoten.text.toString()
            val diachi = binding.diachi.text.toString()
            val sodienthoai = binding.sodienthoai.text.toString()
            savefirestore(hoten, diachi, sodienthoai, uid)
        }
        nam = binding.nam
        nu = binding.nu
        nam.setOnClickListener {
            nam.isChecked = true
            nu.isChecked = false
        }
        nu.setOnClickListener {
            nam.isChecked = false
            nu.isChecked = true
        }
        return root
    }



    private fun savefirestore(hoten: String?, diachi: String?, sodienthoai: String?, uid: String?,){
        //checkbox nam nữ
        nam = binding.nam
        nu = binding.nu

        val db = FirebaseFirestore.getInstance()

        val user: MutableMap<String, Any> = HashMap()
        user["hoten"] =  hoten!!
        user["diachi"] = diachi!!
        user["sodienthoai"] = sodienthoai!!


        if(nam.isChecked()){
            val gioitinh: String? = "Nam"
            user["gioitinh"] = gioitinh!!
        }else if(nu.isChecked()){
            val gioitinh: String? = "Nữ"
            user["gioitinh"] = gioitinh!!
        }else{
            user["gioitinh"] = ""
        }

        db.collection("users").document(uid!!)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(mContext, "THÀNH CÔNG", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "Thất bại", Toast.LENGTH_SHORT).show()
            }
    }
    private fun readFirestoreData(uid: String?){
        val txtDiachi = binding.diachi
        val sodienthoai = binding.sodienthoai
        val hoten = binding.hoten
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    if (document.data?.getValue("diachi").toString() == "null"){
                        txtDiachi.setText("")
                    }else{
                        txtDiachi.setText(document.data?.getValue("diachi").toString())
                    }
                    if (document.data?.getValue("sodienthoai").toString() == "null"){
                        sodienthoai.setText("")
                    }else{
                        sodienthoai.setText(document.data?.getValue("sodienthoai").toString())
                    }
                    if (document.data?.getValue("hoten").toString() == "null"){
                        hoten.setText("")
                    }else{
                        hoten.setText(document.data?.getValue("hoten").toString())
                    }

                    val gioitinh : String = document.data?.getValue("gioitinh").toString()

                    if (gioitinh == "Nam"){
                        nam.isChecked = true
                        nu.isChecked = false
                    }else if(gioitinh == "Nữ"){
                        nam.isChecked = false
                        nu.isChecked = true
                    }else if(gioitinh == "null"){
                        nam.isChecked = false
                        nu.isChecked = false
                    }
                }
            }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                var resolver = requireActivity().contentResolver
                val bitmap = MediaStore.Images.Media.getBitmap(resolver, filePath)
                val imageView: CircleImageView = binding.avatar
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage(){
        val progressDialog = ProgressDialog(mContext)
        progressDialog.setTitle("Thực phẩm nhanh")
        progressDialog.setMessage("Đang tải lên hình ảnh, vui lòng đợi")
        if(filePath != null){
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            progressDialog.show()

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Toast.makeText(mContext, "Thay đổi avatar thành công", Toast.LENGTH_SHORT).show()
                    val user = Firebase.auth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        photoUri = Uri.parse(downloadUri.toString())
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(mContext, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                } else {
                    // Handle failures
                    // ...
                }
            }
        }else{
            Toast.makeText(mContext, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

}
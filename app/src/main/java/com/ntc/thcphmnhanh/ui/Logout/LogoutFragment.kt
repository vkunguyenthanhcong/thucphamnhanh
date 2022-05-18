package com.ntc.thcphmnhanh.ui.Logout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.ntc.thcphmnhanh.Login
import com.ntc.thcphmnhanh.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment(){
    private var _binding: FragmentLogoutBinding? = null

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
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textLogout

        FirebaseAuth.getInstance().signOut()
        val intent = Intent(mContext, Login::class.java)
        startActivity(intent)

        return root
    }
}
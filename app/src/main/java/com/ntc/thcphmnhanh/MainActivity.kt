package com.ntc.thcphmnhanh


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.ntc.thcphmnhanh.databinding.ActivityMainBinding
import android.view.Menu as Menu

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + "0935704083")
                startActivity(dialIntent)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val user = Firebase.auth.currentUser
        val uid = user?.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    if (document.data?.getValue("permission").toString() == "user"){
                        val nav_menu : Menu =  navView.getMenu()
                        nav_menu.findItem(R.id.nav_admin).isVisible = false
                        nav_menu.findItem(R.id.nav_xacnhan).isVisible = false
                        nav_menu.findItem(R.id.nav_themsp).isVisible = false
                    }
                }
            }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_admin, R.id.nav_cart, R.id.nav_donhang, R.id.xacnhan
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
     

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
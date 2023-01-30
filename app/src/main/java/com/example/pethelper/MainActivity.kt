package com.example.pethelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pethelper.databinding.MainActivityBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.recycler_view.*


class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : MainActivityBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //DICHIARAZIONE VARIABILI
        val drawerLayout: DrawerLayout = findViewById(R.id.home_drawer)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val loginButton : Button = findViewById(R.id.loginbutton)
        val signupButton : Button = findViewById(R.id.signupbutton)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)

        toggle.isDrawerIndicatorEnabled
        toggle.syncState()

        //AGGIUNGERE I LISTENER
        loginButton.setOnClickListener {

            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)

        }

        signupButton.setOnClickListener {

            val intent = Intent(this@MainActivity, Signup::class.java)
            startActivity(intent)

        }

        //IMPOSTA IL LISTENER PER IL CLICK DELLA NAV VIEW
        navView.setNavigationItemSelectedListener(this)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
                R.id.profile ->{

                    if(auth.currentUser!=null){
                        val intent = Intent(this@MainActivity, Profile::class.java)
                        intent.putExtra("user", auth.currentUser?.uid.toString())
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Login to see your profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                R.id.add_pet -> {
                    if(auth.currentUser!=null){
                        val intent = Intent(this@MainActivity, AddPet::class.java)
                        intent.putExtra("user", auth.currentUser?.uid.toString())
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Login to see your profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                R.id.log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                }
                R.id.rate_us -> Toast.makeText(
                    applicationContext, "Function not yet implemented", Toast.LENGTH_SHORT)
                    .show()
                R.id.about_us -> Toast.makeText(
                    applicationContext, "Function not yet implemented", Toast.LENGTH_SHORT)
                    .show()
                R.id.dark_theme -> Toast.makeText(
                    applicationContext, "Function not yet implemented", Toast.LENGTH_SHORT)
                    .show()
                R.id.account -> {
                    val intent = Intent(this@MainActivity, Signup::class.java)
                    startActivity(intent)
                }
            }
        return true
    }

}
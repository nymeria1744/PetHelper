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
    private lateinit var reference : DatabaseReference
    private lateinit var database : FirebaseDatabase
    private lateinit var petList : ArrayList<Pet>

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

        //IMPOSTA IL LISTENER PER IL CLICK SULL'OGGETTO SELEZIONATO
        navView.setNavigationItemSelectedListener(this)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /*fun removeItem(view: View) {
        val index = Random.nextInt(8)

        placeholderList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }*/

    /* override fun onItemClick(position: Int) {
        Toast.makeText(this,"Item $position clicked!",Toast.LENGTH_SHORT).show()
        val clickedItem = placeholderList[position]
        clickedItem.title = "Clicked"
        adapter.notifyItemChanged(position)
    }*/



    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this@MainActivity, AddPet::class.java)
                    startActivity(intent)
                }

                R.id.log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                }
                R.id.rate_us -> Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT)
                    .show()
                R.id.about_us -> Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT)
                    .show()
                R.id.dark_theme -> Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT)
                    .show()
                R.id.account -> {
                    val intent = Intent(this@MainActivity, Signup::class.java)
                    startActivity(intent)
                }
            }
        return true
    }




    private fun generatePetList(owner: String, database : FirebaseDatabase): ArrayList<Pet> {

        reference = database.getReference("Userspets")

        reference.child(owner).get()
        .addOnSuccessListener { it ->

            it.children.forEach {

                val id = it.child("id").value.toString()
                val name = it.child("name").value.toString()
                val image = it.child("imageResource").value.toString()
                val birthday = it.child("birthday").value.toString()
                val species = it.child("species").value.toString()
                val sex = it.child("sex").value.toString()

                Toast.makeText(this, "name is $name", Toast.LENGTH_SHORT).show()

                if(!petList.contains(Pet(birthday, id, image, name, owner, sex, species))){

                    petList.add(Pet(birthday, id, image, name, owner, sex, species))
                    Toast.makeText(this, "pet $name added", Toast.LENGTH_SHORT).show()

                }

            }
        }.addOnFailureListener{

            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
        }

        return petList
    }

//    private fun generatePetList1(owner: String, database : FirebaseDatabase): ArrayList<Pet> {
//
//        reference = database.getReference("Userspets")
//
//        reference.child(owner).get().addOnSuccessListener { it ->
//
//            it.children.forEach {
//
//                val id = it.child("id").value.toString()
//                val name = it.child("name").value.toString()
//                val image = it.child("imageResource").value.toString()
//                val birthday = it.child("birthday").value.toString()
//                val species = it.child("species").value.toString()
//                val sex = it.child("sex").value.toString()
//
//                Toast.makeText(this, "name is $name", Toast.LENGTH_SHORT).show()
//
//                if(!petList.contains(Pet(birthday, id, image, name, owner, sex, species))){
//
//                    petList.add(Pet(birthday, id, image, name, owner, sex, species))
//
//                }
//            }
//        }.addOnFailureListener{
//
//            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
//        }
//        return petList
//    }



}
package com.example.pethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pethelper.databinding.AddPetBinding
import com.example.pethelper.databinding.ProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile.*
import kotlinx.android.synthetic.main.recycler_view.*

class Profile : AppCompatActivity(), Adapter.OnItemClickListener {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var petList: ArrayList<Pet>
    private lateinit var adapter: Adapter
    private lateinit var binding: ProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val owner = bundle!!.getString("user").toString()

        binding.addPetButton.setOnClickListener {
            val intent = Intent(this@Profile, AddPet::class.java)
            startActivity(intent)
        }

        petList = ArrayList()
//        petList = generatePetList(owner)
//
//        adapter = Adapter(petList, this)
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

//        petList = getPetList(owner)
//        adapter = Adapter(petList, this)
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

//    private fun getPetList(owner: String) : ArrayList<Pet> {
//        database = FirebaseDatabase.getInstance()
//        reference = database.getReference("Userspets").child(owner)
//        val list = ArrayList<Pet>()
//
//        reference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(data in snapshot.children){
//
//                    val id = data.child("id").value.toString()
//                    val name = data.child("name").value.toString()
//                    val image = data.child("imageResource").value.toString()
//                    val birthday = data.child("birthday").value.toString()
//                    val species = data.child("species").value.toString()
//                    val sex = data.child("sex").value.toString()
//
//                    val pet = Pet(birthday, id, image, name, owner, sex, species)
//
//                    list.add(pet)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("cancelled", error.toString())
//            }
//
//        })
//        return list
//    }

    override fun onStart() {
        super.onStart()

        petList = generatePetList(Firebase.auth.currentUser?.uid.toString())

        adapter = Adapter(petList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun generateList(size: Int): ArrayList<Pet> {

        val list = ArrayList<Pet>()

        val owner = Firebase.auth.currentUser?.uid.toString()

        for (i in 0 until size) {
            val drawable = R.drawable.ic_baseline_pets_24.toString()
            val item = Pet(
                "01/01/2000",
                "00111",
                drawable,
                "Diablo",
                owner,
                "male",
                "Dog"
            )
            list.add(item)
        }

        return list
    }

    private fun generatePetList(owner: String): ArrayList<Pet> {

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Userspets")

        reference.child(owner).get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    it.children.forEach {
                        val id = it.child("id").value.toString()
                        val name = it.child("name").value.toString()
                        val image = it.child("imageResource").value.toString()
                        val birthday = it.child("birthday").value.toString()
                        val species = it.child("species").value.toString()
                        val sex = it.child("sex").value.toString()

                        val pet = Pet(birthday, id, image, name, owner, sex, species)

                        if (!petList.contains(pet)) {
                            petList.add(pet)
                        }

                        Toast.makeText(this, "name is $name", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this, "impossible retrieve data", Toast.LENGTH_SHORT).show()
                }


            }.addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }

        return petList
    }

    //ITEM CLICK LISTENER  FOR ADAPTER
    override fun onItemClick(position: Int) {
        val intent = Intent(this@Profile, Detail::class.java)
        intent.putExtra("title", petList[position].name)
        intent.putExtra("image", petList[position].imageResource)
        intent.putExtra("birthday", petList[position].birthday)
        intent.putExtra("sex", petList[position].sex)
        intent.putExtra("id", petList[position].ID)
        startActivity(intent)
    }
}
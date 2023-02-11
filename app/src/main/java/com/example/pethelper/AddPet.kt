package com.example.pethelper

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pethelper.databinding.AddPetBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*


class AddPet : AppCompatActivity() {

    private lateinit var binding : AddPetBinding
    private lateinit var reference : DatabaseReference
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {

            val uniqueID = UUID.randomUUID().toString()
            val petName = binding.petName.text.toString()
            val birthday = binding.birthday.text.toString()
            val species = binding.species.text.toString()
            val owner = Firebase.auth.currentUser?.uid.toString()

            val sex = if(binding.male.isChecked)
                binding.male.text.toString()
            else if (binding.female.isChecked)
                binding.female.text.toString()
            else
                "?"

            val imageResource =
                when (species) {
                    "Bird" -> R.drawable.ic_bird
                    "Cat" -> R.drawable.ic_cat
                    "Dog" -> R.drawable.ic_dog
                    "Horse" -> R.drawable.ic_horse
                    "Pig" -> R.drawable.ic_pig
                    "Rodent" -> R.drawable.ic_rat
                    "Rabbit" -> R.drawable.ic_rabbit
                    "Fish" -> R.drawable.ic_fish
                    "Turtle" -> R.drawable.ic_turtle
                    "Reptile" -> R.drawable.ic_iguana
                    else -> R.drawable.ic_baseline_pets_24
                }.toString()

            if(uniqueID.trim().isNotEmpty() && petName.trim().isNotEmpty()
                && birthday.trim().isNotEmpty() && species.trim().isNotEmpty()
            ){
                val pet = Pet(birthday, uniqueID, imageResource, petName, owner, sex,species)
                database = FirebaseDatabase.getInstance()
                reference = database.getReference("Pets")

                reference.child(petName).setValue(pet).addOnSuccessListener {
                    binding.petName.text.clear()
                    binding.birthday.text.clear()
                    binding.species.text.clear()
                    binding.female.isChecked = false
                    binding.male.isChecked = false

                    Toast.makeText(
                        this,
                        "Successfully saved pet!",
                        Toast.LENGTH_SHORT)
                        .show()

                    reference = database.getReference("Userspets")
                    reference.child(owner).child(uniqueID).setValue(pet).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Successfully added $petName in it's owner's pet list!",
                            Toast.LENGTH_SHORT)
                            .show()

                    }

                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to save pet", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "please make sure every box is filled properly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val petSpecies = resources.getStringArray(R.array.species)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, petSpecies)
        binding.species.setAdapter(adapter)
    }






}
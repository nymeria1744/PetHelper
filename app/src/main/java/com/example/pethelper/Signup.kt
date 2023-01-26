package com.example.pethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.pethelper.databinding.SignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

    private lateinit var binding: SignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var reference : DatabaseReference
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        //INIZIALIZZAZIONE
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        //CLICK LISTENER
        binding.backButton.setOnClickListener{
            finish()
        }

        binding.signupbutton.setOnClickListener {

            val username = binding.signupusername.text.toString()
            val email = binding.signupemail.text.toString()
            val password = binding.signuppassword.text.toString()
            val confirmPass = binding.confirmsignuppassword.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if(username.isNotEmpty()){
                    if(password.length >= 6){
                        if (password == confirmPass) {

                            val user = User(username, email, password)
                            database = FirebaseDatabase.getInstance()
                            reference = database.getReference("Users")

                            reference.child(username).setValue(user).addOnSuccessListener {
                                binding.signupusername.text.clear()
                                binding.signupemail.text.clear()
                                binding.signuppassword.text.clear()
                                binding.confirmsignuppassword.text.clear()

                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {
                                    if (it.isSuccessful){

                                        reference = database.getReference("Userspets")
                                        reference.child(email).setValue(email)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this,
                                                    "Petlist for $email created!",
                                                    Toast.LENGTH_SHORT)
                                                    .show()
                                        }

                                        Toast.makeText(
                                            this,
                                            "Successfully saved user",
                                            Toast.LENGTH_SHORT)
                                            .show()

                                        finish()

                                    }else{
                                        Toast.makeText(
                                            this,
                                            it.exception.toString(),
                                            Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show()
                            }


                        } else {
                            Toast.makeText(this, "Password is not matching!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Username is empty!", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(
                    this,
                    "Please enter a valid email",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


    }
}
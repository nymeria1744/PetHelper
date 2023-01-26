package com.example.pethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.pethelper.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.pet_schedule.view.*


class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //INIZIALIZZAZIONE
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //se è già presente il currentUser, inserisce l'indirizzo email del suddetto nell'edit text
        if(Firebase.auth.currentUser != null){
            binding.loginemail.setText(Firebase.auth.currentUser?.email.toString())
        }


        //CLICK LISTENER
        binding.signupbutton.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }

        binding.loginbutton.setOnClickListener {

            val email = binding.loginemail.text.toString()
            val password = binding.loginpassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "logged in as $email", Toast.LENGTH_SHORT).show()
                        //INSERIRE LA CREAZIONE DELLA PETLIST
                       finish()
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Please fill all the fields before registering!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}
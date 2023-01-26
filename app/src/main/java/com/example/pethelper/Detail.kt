package com.example.pethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.pethelper.databinding.DetailsBinding

class Detail : AppCompatActivity() {

    private lateinit var binding: DetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //estrae dal bundle le informazioni messe come extras
        val bundle : Bundle?= intent.extras
        val image = bundle!!.getString("image")
        val name = bundle.getString("title")
        val birthday = bundle.getString("birthday")
        val sex = bundle.getString("sex")
        val id = bundle.getString("id")

        //TRASFORMA L'IMAGE RESOURCE DA STRINGA A INT
        val imageRes: Int = java.lang.String.valueOf(image).toInt()

        //assegna alla layout i valori degli extras
        binding.petImage.setImageResource(imageRes)
        binding.petName.text = name
        binding.petBirthday.text = birthday
        binding.petGender.text = sex

        binding.scheduleButton.setOnClickListener {
            val intent = Intent(this@Detail, PetSchedule::class.java)
            intent.putExtra("petName", name)
            intent.putExtra("ID", id)
            startActivity(intent)

        }

    }
}
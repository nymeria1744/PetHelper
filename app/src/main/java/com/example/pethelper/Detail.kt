package com.example.pethelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pethelper.databinding.DetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Detail : AppCompatActivity(), Adapter.OnItemClickListener,
    ScheduleAdapter.OnItemClickListener {

    private lateinit var binding: DetailsBinding
    private lateinit var schedule: ArrayList<Schedule>
    private lateinit var adapter : ScheduleAdapter
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //estrae dal bundle le informazioni messe come extras
        val bundle : Bundle?= intent.extras
        val image = bundle!!.getString("image")
        val name = bundle.getString("title").toString()
        val birthday = bundle.getString("birthday").toString()
        val sex = bundle.getString("sex").toString()
        val petId = bundle.getString("id").toString()

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
            intent.putExtra("petId", petId)
            startActivity(intent)

        }

        getScheduleFromFirebase(
            petId = petId,
            callback = object : OnDataReceiveCallback {
                override fun onDataReceived(schedule: ArrayList<Schedule>) {
                    this@Detail.schedule = schedule
                    adapter = ScheduleAdapter(schedule, this@Detail)
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@Detail)
                }
            }
        )

    }

    interface OnDataReceiveCallback {
        fun onDataReceived(scheduleList: ArrayList<Schedule>)
    }

    private fun getScheduleFromFirebase(petId: String, callback: OnDataReceiveCallback) {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Petschedule").child(petId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val scheduleList = arrayListOf<Schedule>()

                dataSnapshot.children.forEach() {
                    val id = it.child("id").value.toString()
                    val activity = it.child("activity").value.toString()
                    val days = it.child("days").value.toString()
                    val pet = it.child("pet").value.toString()
                    val time = it.child("time").value.toString()
                    val notify = it.child("notify").value as Boolean

                    val schedule = Schedule(id, pet, activity, days, time, notify)

                    scheduleList.add(schedule)

                }

                callback.onDataReceived(scheduleList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Detail, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


 //ITEM CLICK FOR ADAPTER
    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }


}
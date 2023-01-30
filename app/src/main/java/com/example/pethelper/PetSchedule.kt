package com.example.pethelper

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.pethelper.databinding.AddPetBinding
import com.example.pethelper.databinding.PetScheduleBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.pet_schedule.*
import kotlinx.android.synthetic.main.pet_schedule.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class PetSchedule : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: PetScheduleBinding
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("hh:mm a", Locale.ITALY)
    private lateinit var reference : DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var days : String
    private lateinit var time : String
    private var notify : Boolean = false
    private var clicked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PetScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val petId = bundle!!.getString("petId").toString()
        val petName = bundle.getString("petName").toString()

        val timeButton: Button = findViewById(R.id.time_button)
        timeButton.setOnClickListener {

            clicked = true

            TimePickerDialog(
                this,
                AlertDialog.BUTTON_NEUTRAL,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()

        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            //CONTROLLARE CHE TIME BUTTON SIA STATO UTILIZZATO
            if(clicked){

                val activity = binding.activity.text.toString()
                days = selectDays()
                val uniqueID = UUID.randomUUID().toString()
                time = binding.timeButton.text.toString()
                notify = binding.checkboxNotify.isChecked

                val schedule = Schedule(uniqueID,petId,activity,days,time,notify)
                database = FirebaseDatabase.getInstance()
                reference = database.getReference("Schedule")

                //INSERIRE IL NOME DEL PET TRAMITE PUT EXTRA NELL'INTENT DI DETAILS
                reference.child(uniqueID).setValue(schedule).addOnSuccessListener {

                    binding.activity.text.clear()
                    binding.timeButton.text = ""

                    binding.checkboxMonday.isChecked = false
                    binding.checkboxTuesday.isChecked = false
                    binding.checkboxWednesday.isChecked = false
                    binding.checkboxThursday.isChecked = false
                    binding.checkboxFriday.isChecked = false
                    binding.checkboxSaturday.isChecked = false
                    binding.checkboxSunday.isChecked = false

                    binding.checkboxNotify.isChecked = false

                    //crea un istanza in Petschedule
                    reference = database.getReference("Petschedule")
                    reference.child(petId).child(uniqueID).setValue(schedule).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Successfully added $petName's schedule",
                            Toast.LENGTH_SHORT)
                            .show()

                    }

                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, "failed to add schedule", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "You need to select time first", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun selectDays() : String {
        var selectedDays = ""

        if (binding.checkboxMonday.isChecked) {
            selectedDays += binding.checkboxMonday.text.toString()+" "
        }
        if (binding.checkboxTuesday.isChecked) {
            selectedDays += binding.checkboxTuesday.text.toString()+" "
        }
        if (binding.checkboxWednesday.isChecked) {
            selectedDays += binding.checkboxWednesday.text.toString()+" "
        }
        if (binding.checkboxThursday.isChecked) {
            selectedDays += binding.checkboxThursday.text.toString()+" "
        }
        if (binding.checkboxWednesday.isChecked) {
            selectedDays += binding.checkboxFriday.text.toString()+" "
        }
        if (binding.checkboxSaturday.isChecked) {
            selectedDays += binding.checkboxSaturday.text.toString()+" "
        }
        if (binding.checkboxSunday.isChecked) {
            selectedDays += binding.checkboxSunday.text.toString()
        }
        return selectedDays
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        displayFormattedTime(calendar.timeInMillis)
    }

    private fun displayFormattedTime(timestamp: Long) {
        findViewById<Button>(R.id.time_button).text = formatter.format(timestamp)
        Log.i("Formatting", timestamp.toString())

    }
}
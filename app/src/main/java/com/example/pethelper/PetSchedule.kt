package com.example.pethelper

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TimePicker
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

        val timeButton: Button = findViewById(R.id.time_button)
        timeButton.setOnClickListener {

            clicked = true

            TimePickerDialog(
                this,
                AlertDialog.THEME_HOLO_DARK,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()

        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this@PetSchedule, AddPet::class.java)
            startActivity(intent)
        }

        binding.saveButton.setOnClickListener {
            //CONTROLLARE CHE TIME BUTTON SIA STATO UTILIZZATO
            if(clicked){

                val activity = binding.activity.text.toString()
                days = selectDays()
                val uniqueID = UUID.randomUUID().toString()
                time = binding.timeButton.text.toString()
                notify = binding.checkboxNotify.isChecked

                val schedule = Schedule(uniqueID,"",activity,days,time,notify)
                database = FirebaseDatabase.getInstance()
                reference = database.getReference("Schedule")

                //INSERIRE IL NOME DEL PET TRAMITE PUT EXTRA NELL'INTENT DI DETAILS
                reference.child("").setValue(schedule).addOnSuccessListener {

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

                    finish()
                }

            }

        }

    }

    private fun selectDays() : String {
        var selectedDays = ""

        if (findViewById<CheckBox>(R.id.checkbox_monday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_monday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_tuesday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_tuesday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_wednesday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_wednesday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_thursday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_thursday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_friday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_friday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_saturday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_saturday).text.toString()+", "
        }
        if (findViewById<CheckBox>(R.id.checkbox_sunday).isChecked) {
            selectedDays += findViewById<CheckBox>(R.id.checkbox_sunday).text.toString()
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
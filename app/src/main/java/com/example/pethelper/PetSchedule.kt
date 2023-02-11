package com.example.pethelper

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.pethelper.databinding.PetScheduleBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PetSchedule : AppCompatActivity() {

    private lateinit var binding: PetScheduleBinding
    private val calendar = Calendar.getInstance()
    private lateinit var reference : DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var notify : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PetScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val petId = bundle!!.getString("petId").toString()
        val petName = bundle.getString("petName").toString()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {

            if(binding.activity.text.isNotEmpty()){
                val activity = binding.activity.text.toString()
                val uniqueID = UUID.randomUUID().toString()

                val zeroDay = if(binding.datePicker.dayOfMonth < 10){
                    "0"
                }else{
                    ""
                }
                val days = zeroDay + binding.datePicker.dayOfMonth.toString() + "/" + (binding.datePicker.month+1).toString()

                val zeroMinute = if(binding.timePicker.minute < 10){
                    "0"
                } else{
                    ""
                }
                val time = binding.timePicker.hour.toString() + ":$zeroMinute" + binding.timePicker.minute.toString()

                notify = binding.checkboxNotify.isChecked

                val schedule = Schedule(uniqueID,petId,activity,days,time,notify)

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("Schedule")

                //INSERIRE IL NOME DEL PET TRAMITE PUT EXTRA NELL'INTENT DI DETAILS
                reference.child(uniqueID).setValue(schedule).addOnSuccessListener {

                    binding.activity.text.clear()

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

                }.addOnFailureListener {
                    Toast.makeText(this, "failed to add schedule", Toast.LENGTH_SHORT).show()
                }

                if(notify){
                    //se l'utente vuole ricevere una notifica...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel()
                    }
                    scheduleNotification(petName)
                }

                finish()

            }else{
                Toast.makeText(this, "Enter an activity name pls", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun scheduleNotification(petName: String) {

        val intent = Intent(applicationContext, Notification::class.java)
        val title = "$petName needs you!"
        val message = binding.activity.text.toString()
        intent.putExtra(messageExtra, message)
        intent.putExtra(titleExtra, title)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        //showAlert(time, title, message)
    }

//    private fun showAlert(time: Long, title: String, message: String) {
//        val date = Date(time)
//        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
//        val timeFormat = DateFormat.getTimeFormat(applicationContext)
//
//        AlertDialog.Builder(this)
//            .setTitle("Notification Scheduled")
//            .setMessage(
//                "Title: " + title +
//                "\nMessage: " + message +
//                "\nAt: "+ dateFormat.format(date) + " " + timeFormat.format(date)
//            ).setPositiveButton("OK"){_,_ ->}
//    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        calendar.set(year, month, day, hour, minute)

        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

        val name = "Notification Channel"
        val desc = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel =
            NotificationChannel(channelId, name, importance)

        channel.description = desc

        val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.createNotificationChannel(channel)

    }

}
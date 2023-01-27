package com.example.pethelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ScheduleAdapter(
    private val scheduleList: List<Schedule>,
    private val listener: OnItemClickListener
):
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_schedule,
            parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = scheduleList[position]

        holder.activityName.text = currentItem.activity
        holder.selectedDays.text = currentItem.days
        holder.time.text = currentItem.time

    }

    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ):
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val activityName: TextView = itemView.findViewById(R.id.activityName)
        val selectedDays: TextView = itemView.findViewById(R.id.pet_name)
        val time: TextView = itemView.findViewById(R.id.time)

        //si preferisce scrivere i metodi di setOnClickListener qui dato che questo metodo viene
        // chiamato solo poche volte nel codice a differenza del onBindViewHolder
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            //CONTROLLIAMO CHE LA POSIZIONE NON SIA INVALIDA, NON DOVREBBE COMUNQUE ACCADERE
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    override fun getItemCount() = scheduleList.size

}
package com.example.pethelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        holder.deleteButton.setOnClickListener{
            listener.onItemClick(position)
        }

    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener)
        : RecyclerView.ViewHolder(itemView) {
        val activityName: TextView = itemView.findViewById(R.id.activityName)
        val selectedDays: TextView = itemView.findViewById(R.id.days)
        val time: TextView = itemView.findViewById(R.id.time)
        val deleteButton : Button = itemView.findViewById(R.id.done_button)

    }

    override fun getItemCount() = scheduleList.size

}
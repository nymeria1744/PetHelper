package com.example.pethelper

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val petList: List<Pet>,
    private val listener: OnItemClickListener
):
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view,
            parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = petList[position]

        val imageRes: Int = java.lang.String.valueOf(currentItem.imageResource).toInt()

        holder.petImage.setImageResource(imageRes)
        holder.petName.text = currentItem.name
    }

    override fun getItemCount() = petList.size

    //INNER PREVIENE L'USO DEL VIEW HOLDER AL DI FUORI DELLA NOSTRA CALSSE ADAPTER
    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ):
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val petImage: ImageView = itemView.findViewById(R.id.pet_picture)
        val petName: TextView = itemView.findViewById(R.id.pet_name)

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

}
package com.example.ttshfypj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ttshfypj.R
import com.example.ttshfypj.data_class.reminders
import kotlinx.android.synthetic.main.list_reminder.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class reminderAdapter(
    val reminderlist: ArrayList<reminders>,
    val itemClickHandler: (String, String, View) -> Unit
) : RecyclerView.Adapter<reminderAdapter.ViewHolder>() {

    val LocalDateNow = LocalDate.now()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_reminder, parent, false)
        val holder = ViewHolder(v)

        holder.takenornot.setOnClickListener {
            itemClickHandler.invoke(
                reminderlist[holder.adapterPosition].docid,
                reminderlist[holder.adapterPosition].LastTaken,
                v
            )
        }

        return holder

    }

    override fun getItemCount(): Int {
        return reminderlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder: reminders = reminderlist[position]
        holder.medicineName.text = reminder.medicineN
        holder.medicineSchedule.text = reminder.MedicineTime
        holder.medicineDosage.text = reminder.MedicineDosage
        val imagemedicine = holder.itemView.MedicineImageView
        Glide.with(imagemedicine).load(reminder.MedicineImage).into(imagemedicine);

        if (reminder.LastTaken == null || reminder.LastTaken == "") {

        } else {
            val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
            val formatteddate = LocalDate.parse(reminder.LastTaken, formatter)


            if (LocalDateNow == formatteddate) {
                holder.takenornot.setImageResource(R.drawable.takenmedicine)
            }
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineName = itemView.findViewById(R.id.MedicineName) as TextView
        val medicineSchedule = itemView.findViewById(R.id.MedicineSchedule) as TextView
        val medicineDosage = itemView.findViewById(R.id.MedicineDosage) as TextView
        val takenornot = itemView.findViewById(R.id.takemedicine) as ImageView


    }

}

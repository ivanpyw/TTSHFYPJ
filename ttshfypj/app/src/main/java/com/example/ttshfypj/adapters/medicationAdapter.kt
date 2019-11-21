package com.example.ttshfypj.adapters

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ttshfypj.data_class.medications
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ttshfypj.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.list_medication.view.*
import com.google.firebase.storage.StorageReference
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class medicationAdapter(
    val medicationlist: ArrayList<medications>,
    val itemClickHandler: (String) -> Unit
) : RecyclerView.Adapter<medicationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val headerView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_medication, parent, false)
        val headerViewHolder = ViewHolder(headerView)
        headerView.setOnClickListener {
            itemClickHandler.invoke(medicationlist[headerViewHolder.adapterPosition].docid)
        }
        return headerViewHolder
    }

    override fun getItemCount(): Int {
        return medicationlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val medicationgroup: medications = medicationlist[position]
        holder.medicationnamevar.text = medicationgroup.medicineN
        holder.medicationschedulevar.text = medicationgroup.MedicineTime

        holder.medicationstatusholder.text = medicationgroup.finish
        val imagemedicine = holder.itemView.medicationimageview
        Glide.with(imagemedicine).load(medicationgroup.MedicineImage).fitCenter()
            .into(imagemedicine);

        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        val formatteddate = LocalDate.parse(medicationgroup.expirydate, formatter)
        val localdatenow = LocalDate.now()


        if (medicationgroup.finish == "ongoing") {
            if (localdatenow.isAfter(formatteddate)) {
                holder.medicationcardview.setCardBackgroundColor(Color.parseColor("#EF7F80"))
            }
        } else {
            holder.medicationcardview.setCardBackgroundColor(Color.parseColor("#B4FFB2"))
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationnamevar = itemView.findViewById(R.id.medicationname) as TextView
        val medicationschedulevar = itemView.findViewById(R.id.medicationschedule) as TextView
        val medicationcardview = itemView.findViewById(R.id.medicationcardview) as CardView
        val medicationstatusholder = itemView.findViewById(R.id.medicationstatus) as TextView

    }
}
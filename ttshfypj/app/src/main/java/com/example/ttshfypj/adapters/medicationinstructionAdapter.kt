package com.example.ttshfypj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ttshfypj.R
import com.example.ttshfypj.data_class.instruction
import kotlinx.android.synthetic.main.list_medicineinstructions.view.*

class medicationinstructionAdapter(val instructionlist: ArrayList<instruction>) :
    RecyclerView.Adapter<medicationinstructionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val headerView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_medicineinstructions, parent, false)
        val headerViewHolder = ViewHolder(headerView)
        return headerViewHolder
    }

    override fun getItemCount(): Int {
        return instructionlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instructionlists: instruction = instructionlist[position]

        holder.instructiontitle.text = instructionlists.instructiontitle
        holder.instructiondesc.text = instructionlists.instructiondesc
        val helpimage = holder.itemView.medicationinstructionimageview

        if (instructionlists.instructionimage == null || instructionlists.instructionimage == "") {
            helpimage.visibility = View.GONE
        } else {
            Glide.with(helpimage).load(instructionlists.instructionimage).fitCenter()
                .into(helpimage);
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instructiontitle = itemView.findViewById(R.id.medicationinstructiontitle) as TextView
        val instructiondesc =
            itemView.findViewById(R.id.medicationinstructiondescription) as TextView
    }
}
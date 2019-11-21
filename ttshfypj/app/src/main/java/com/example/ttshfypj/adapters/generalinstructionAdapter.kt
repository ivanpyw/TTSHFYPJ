package com.example.ttshfypj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ttshfypj.R
import com.example.ttshfypj.data_class.generalinstruction
import kotlinx.android.synthetic.main.list_generalinstruction.view.*
import kotlinx.android.synthetic.main.list_medicineinstructions.view.*

class generalinstructionAdapter(val instructionlist: ArrayList<generalinstruction>) :
    RecyclerView.Adapter<generalinstructionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val headerView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_generalinstruction, parent, false)
        val headerViewHolder = ViewHolder(headerView)
        return headerViewHolder
    }

    override fun getItemCount(): Int {
        return instructionlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val generalinstructionlist: generalinstruction = instructionlist[position]

        holder.instructiontitle.text = generalinstructionlist.instructiontitle
        holder.instructiondesc.text = generalinstructionlist.instructiondesc
        val helpimage = holder.itemView.generalinstructionimage

        if (generalinstructionlist.instructionimage == null || generalinstructionlist.instructionimage == "") {
            helpimage.visibility = View.GONE
        } else {
            Glide.with(helpimage).load(generalinstructionlist.instructionimage).fitCenter()
                .into(helpimage);
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instructiontitle = itemView.findViewById(R.id.generalinstructiontitle) as TextView
        val instructiondesc = itemView.findViewById(R.id.generalinstructiondesc) as TextView
    }
}
package com.example.ttshfypj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ttshfypj.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ttshfypj.data_class.generalhelpfirstlayer
import kotlinx.android.synthetic.main.list_generalhelp.view.*


class generalhelpAdapter(
    val generalhelplist: ArrayList<generalhelpfirstlayer>,
    val itemClickHandler: (String) -> Unit
) : RecyclerView.Adapter<generalhelpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val headerView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_generalhelp, parent, false)
        val headerViewHolder = ViewHolder(headerView)
        headerView.setOnClickListener {
            itemClickHandler.invoke(generalhelplist[headerViewHolder.adapterPosition].docid)
        }
        return headerViewHolder
    }

    override fun getItemCount(): Int {
        return generalhelplist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val helplist: generalhelpfirstlayer = generalhelplist[position]

        holder.generalhelptitle.text = helplist.helptitle
        holder.generalhelpdescription.text = helplist.helpdescription
        val helpimage = holder.itemView.generalhelpimageview

        if (helplist.helpimage != null || helplist.helpimage != "") {
            Glide.with(helpimage).load(helplist.helpimage).centerInside().into(helpimage);
        } else {
            helpimage.visibility = View.GONE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val generalhelptitle = itemView.findViewById(R.id.generalhelptitle) as TextView
        val generalhelpdescription = itemView.findViewById(R.id.generalhelpdescription) as TextView
    }
}
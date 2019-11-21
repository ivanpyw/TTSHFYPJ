package com.example.ttshfypj.front_end

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.adapters.reminderAdapter
import com.example.ttshfypj.data_class.reminders
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.list_reminder.view.*
import kotlinx.android.synthetic.main.reminder_fragment.view.*
import kotlin.collections.ArrayList
import java.time.*
import java.time.format.DateTimeFormatter

import android.content.DialogInterface

import android.app.AlertDialog
import androidx.cardview.widget.CardView


/**
 * A simple [Fragment] subclass.
 */
class reminder_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val db = FirebaseFirestore.getInstance()
        val v = inflater.inflate(R.layout.reminder_fragment, container, false)
        val recyclerView = v.ReminderRecyclerView as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val documentspecific = sharedPreference.getValueString("patientspecific")!!
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar

        prog.setVisibility(View.INVISIBLE);
        queryFirebase(recyclerView, prog, documentspecific)

        return v
    }


    fun queryFirebase(
        recyclerView: RecyclerView,
        prog: ProgressBar,
        documentspecific: String

    ) {
        showLoading(prog)
        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val db = FirebaseFirestore.getInstance()
        val reminderlist = ArrayList<reminders>()
        val nowtime = LocalTime.now()


        db.collection("PatientList").document(documentspecific).collection("Medicine")
            .whereEqualTo("medicinestatus", "ongoing")
            .get()
            .addOnSuccessListener { result ->
                var index = 0
                for (document in result) {

                    val medicineschedule = document.data["medicineschedule"].toString()
                    val medicineformattedtime = LocalTime.parse(medicineschedule)

                    val diff = Duration.between(nowtime, medicineformattedtime)

                    if (diff.toHours() <= 1 && diff.toHours() >= -1) {
                        index++
                        val medicinedosage = document.data["medicinedosage"].toString()
                        val medicineimage = document.data["medicineimage"].toString()
                        val medicinename = document.data["medicinename"].toString()
                        val medicineconfirmschedule =
                            document.data["medicineschedule"].toString()
                        val lasttaken = document.data["lasttaken"].toString()
                        val id = document.id

                        reminderlist.add(
                            reminders(
                                medicineimage,
                                medicinename,
                                medicineconfirmschedule,
                                medicinedosage,
                                lasttaken,
                                id
                            )


                        )
                    }

                }


                val adapter =
                    reminderAdapter(reminderlist, this@reminder_fragment::onItemClickHandler)
                recyclerView.adapter = adapter
                hideLoading(prog)
            }
    }

    private fun onItemClickHandler(position: String, lasttaken: String, view: View) {
        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val db = FirebaseFirestore.getInstance()

        if (lasttaken == null || lasttaken == "") {

            val documentspecific = sharedPreference.getValueString("patientspecific")!!
            val dbstatic =
                db.collection("PatientList").document(documentspecific).collection("Medicine")
                    .document(position)
            val formatteddate =
                LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));

            dbstatic
                .update("lasttaken", formatteddate.toString())
                .addOnSuccessListener {
                    Toast.makeText(context, "Medicine Successfully Taken!", Toast.LENGTH_SHORT)
                        .show()
                    view.takemedicine.setImageResource(R.drawable.takenmedicine)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Medicine not successfully taken!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        } else {
            val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
            val formatteddate = LocalDate.parse(lasttaken, formatter)

            if (LocalDate.now().isEqual(formatteddate)) {
                Toast.makeText(context, "Medicine Already Taken Today", Toast.LENGTH_SHORT).show()
            } else {


                val documentspecific = sharedPreference.getValueString("patientspecific")!!
                val dbstatic =
                    db.collection("PatientList").document(documentspecific).collection("Medicine")
                        .document(position)
                val formatteddate =
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));

                dbstatic
                    .update("lasttaken", formatteddate.toString())
                    .addOnSuccessListener {
                        view.takemedicine.setImageResource(R.drawable.takenmedicine)

                        val builder1 = AlertDialog.Builder(context)
                        builder1.setMessage("Medicine Taken Successful")
                        builder1.setCancelable(true)


                        builder1.setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })

                        val alert11 = builder1.create()
                        alert11.show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Medicine not successfully taken!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }
        }
    }


    fun showLoading(prog: ProgressBar) {
        prog.setVisibility(View.VISIBLE)
    }

    fun hideLoading(prog: ProgressBar) {
        prog.setVisibility(View.GONE)
    }


}




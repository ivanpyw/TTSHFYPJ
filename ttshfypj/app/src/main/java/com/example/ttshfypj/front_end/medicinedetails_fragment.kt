package com.example.ttshfypj.front_end


import android.app.AlertDialog
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.data_class.medications
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.medicinedetails_fragment.*
import kotlinx.android.synthetic.main.medicinedetails_fragment.view.*

import android.view.ContextThemeWrapper


/**
 * A simple [Fragment] subclass.
 */
class medicinedetails_fragment : Fragment() {

    companion object {
        var ARG_medicineid = "userinput"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPreference: SharedPreference = SharedPreference(context!!)

        val v = inflater.inflate(R.layout.medicinedetails_fragment, container, false)

        val medicineid = arguments?.getString(medicinedetails_fragment.ARG_medicineid)
        val specificnamedetail = v.findViewById(R.id.specificname) as TextView
        val specificusesdetail = v.findViewById(R.id.specificuse) as TextView
        val specificsideeffectdetail = v.findViewById(R.id.specificsideeffect) as TextView
        val specificimageview = v.findViewById(R.id.specificimageview) as ImageView
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar
        val BtnFinishMedicine = v.findViewById(R.id.BtnFinishMedicine) as Button
        prog.setVisibility(View.INVISIBLE);
        BtnFinishMedicine.visibility = View.GONE

        val documentspecific = sharedPreference.getValueString("patientspecific")!!

        queryFirebase(
            medicineid!!,
            specificnamedetail,
            specificusesdetail,
            specificsideeffectdetail,
            specificimageview,
            prog,
            documentspecific,
            v
        )

        BtnFinishMedicine.setOnClickListener {
            val builder1 = AlertDialog.Builder(
                ContextThemeWrapper(
                    context,
                    R.style.AlertDialogCustom
                )
            )
            builder1.setMessage("Are you sure you have finished the medicine?")
            builder1.setCancelable(true)


            builder1.setPositiveButton(
                "YES",
                DialogInterface.OnClickListener { dialog, id ->

                    val db = FirebaseFirestore.getInstance()

                    val documentspecific = sharedPreference.getValueString("patientspecific")!!

                    val dbstatic =
                        db.collection("PatientList").document(documentspecific).collection("Medicine").document(medicineid)

                    dbstatic
                        .update("medicinestatus", "finished")
                        .addOnSuccessListener {
                            Toast.makeText(context, "Medicine finished successfully!", Toast.LENGTH_SHORT)
                                .show()
                            BtnFinishMedicine.visibility = View.GONE

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Medicine not successfully updated!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                })

            builder1.setNegativeButton(
                "NO"
            ) { dialog, id -> dialog.cancel() }

            val alert11 = builder1.create()
            alert11.show()


        }



        return v
    }


    fun queryFirebase(
        medicineid: String,
        specificnamedetail: TextView,
        specificusesdetail: TextView,
        specificsideeffectdetail: TextView,
        specificimageview: ImageView,
        prog: ProgressBar,
        documentspecific: String,
        view: View
    ) {
        showLoading(prog)


        val db = FirebaseFirestore.getInstance()
        val documentspecific = documentspecific


        db.collection("PatientList").document(documentspecific).collection("Medicine")
            .document(medicineid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val medicineimage = document.data!!["medicineimage"].toString()
                    Glide.with(this).load(medicineimage)
                        .into(specificimageview)
                    specificnamedetail.setText(document.data!!["medicinename"].toString())
                    specificusesdetail.setText(document.data!!["medicineuse"].toString())
                    specificsideeffectdetail.setText(document.data!!["medicineeffect"].toString())
                    specificexpirydate.setText(document.data!!["expirydate"].toString())
                    specificdosage.setText(document.data!!["medicinedosage"].toString())
                    specificlasttaken.setText(document.data!!["lasttaken"].toString())
                    if (document.data!!["medicinestatus"].toString() == "ongoing")
                    {
                        BtnFinishMedicine.visibility = View.VISIBLE
                    }
                    hideLoading(prog)

                } else {

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

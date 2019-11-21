package com.example.ttshfypj.front_end

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.adapters.medicationAdapter
import com.example.ttshfypj.data_class.medications
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.reminder_fragment.view.*
import android.os.AsyncTask
import java.util.*
import kotlin.collections.ArrayList




/**
 * A simple [Fragment] subclass.
 */
class medications_fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.medications_fragment, container, false)

        val recyclerView = v.ReminderRecyclerView as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar;
        prog.setVisibility(View.INVISIBLE);

        queryFirebase(recyclerView, prog)
        return v
    }

    private fun onItemClickHandler(position: String) {

        var medicinedatabaseid: medicinespecific_fragment? =
            activity?.supportFragmentManager?.findFragmentByTag("medicinespecific_fragment") as medicinespecific_fragment?
        if (medicinedatabaseid == null) {
            var newFragment = medicinespecific_fragment()
            var args = Bundle()
            args.putString(medicinespecific_fragment.ARG_medicinedatabaseid, position)
            newFragment.arguments = args

            var ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.replace(R.id.fragmentplaceholder, newFragment, null)
            ft?.commit()
        }
    }


    fun queryFirebase(recyclerView: RecyclerView, prog: ProgressBar) {
        showLoading(prog)
        val db = FirebaseFirestore.getInstance()
        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val medicationArraylists = ArrayList<medications>()

        val documentspecific = sharedPreference.getValueString("patientspecific")!!



        db.collection("PatientList").document(documentspecific).collection("Medicine")
            .orderBy("medicinestatus", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val medicineimage = document.data["medicineimage"].toString()
                    val medicinename = document.data["medicinename"].toString()
                    val medicineschedule = document.data["medicineschedule"].toString()
                    val medicinedocid = document.id
                    val medicineexpirydate = document.data["expirydate"].toString()
                    val medicinestatus = document.data["medicinestatus"].toString()


                    if (medicinestatus == "ongoing") {
                        val random = Random()
                        val m = random.nextInt(9999 - 1000) + 1000
                        var notifyManager =
                            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        val notificationChannel = NotificationChannel(
                            "unique_channel_id",
                            "channel_name",
                            NotificationManager.IMPORTANCE_HIGH
                        )

                        notifyManager.createNotificationChannel(notificationChannel)

                        var notifyBuilder = NotificationCompat.Builder(
                            context!!.applicationContext,
                            "unique_channel_id"
                        ).setSmallIcon(R.drawable.reminderbell)
                            .setContentTitle(medicinename)
                            .setContentText("Remember to take your " + medicinename + " at: " + medicineschedule)



                        AsyncTask.execute {

                            val futureTarget = Glide.with(context!!.applicationContext)
                                .asBitmap()
                                .load(medicineimage)
                                .submit()

                            val bitmap = futureTarget.get()
                            notifyBuilder.setLargeIcon(bitmap)

                            notifyManager.notify(m, notifyBuilder.build())
                        }
                    }

                        medicationArraylists.add(
                            medications(
                                medicineimage,
                                medicinename,
                                medicineschedule,
                                medicinedocid,
                                medicineexpirydate,
                                medicinestatus
                            )
                        )
                    }


                val adapter = medicationAdapter(
                    medicationArraylists,
                    this@medications_fragment::onItemClickHandler
                )
                recyclerView.adapter = adapter
                hideLoading(prog)
            }

    }

    fun showLoading(prog: ProgressBar) {
        prog.setVisibility(View.VISIBLE)
    }

    fun hideLoading(prog: ProgressBar) {
        prog.setVisibility(View.GONE)
    }


}



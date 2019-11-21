package com.example.ttshfypj.front_end


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.adapters.medicationinstructionAdapter
import com.example.ttshfypj.data_class.instruction
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.medicineinstructions_fragment.view.*
import com.example.ttshfypj.front_end.showLoading as showLoading1
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat.setMediaController

import android.widget.*
import android.view.ViewTreeObserver
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.medicineinstructions_fragment.*
import android.media.MediaPlayer


/**
 * A simple [Fragment] subclass.
 */
class medicineinstructions_fragment : Fragment() {

    companion object {
        var ARG_medicineid = "userinput"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.medicineinstructions_fragment, container, false)

        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val recyclerView = v.medicineinstruction_recyclerview as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val medid = arguments?.getString(medicineinstructions_fragment.ARG_medicineid)
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar;
        val titledetails = v.findViewById(R.id.instructiontitletextview) as TextView;

        prog.setVisibility(View.INVISIBLE);
        titledetails.setVisibility(View.GONE)

        val videoViewLandscape = v.findViewById(R.id.videoview_medicineinstruction) as VideoView
        val videocardview = v.findViewById(R.id.videocardview) as CardView
        val videotext = v.findViewById(R.id.videotextview) as TextView

        videocardview.visibility = View.GONE
        videotext.visibility = View.GONE

        val db = FirebaseFirestore.getInstance()

        db.collection("PatientList").document(sharedPreference.getValueString("patientspecific")!!)
            .collection("Medicine").document(medid!!)
            .get()
            .addOnSuccessListener { document ->

                if (document != null) {

                    val medicinevideolink = document.data!!["medicinevideo"].toString()

                    Log.e(
                        "ERROR TAG",
                        document.data!!["medicinevideo"].toString()
                    )

                    if (medicinevideolink.isNullOrBlank()) {

                        Log.e(
                            "ERROR TAG",
                            document.data!!["medicinevideo"].toString() + " <- DOES THIS HAVE DATA? VIDEO NOT DETECTED"
                        )
                    } else {
                        videocardview.visibility = View.VISIBLE
                        videotext.visibility = View.VISIBLE
                        val video = document.data!!["medicinevideo"].toString()

                        val str = video
                        val mediaController = MediaController(context)

                        mediaController.setAnchorView(videoViewLandscape)
                        videoViewLandscape.setMediaController(mediaController)
                        videoViewLandscape.setVideoURI(Uri.parse(str))
                        videoViewLandscape.seekTo(100);
                        videoViewLandscape.requestFocus()
                        val nestedScroller =
                            v.findViewById(R.id.nestedScrollView) as NestedScrollView

                        nestedScroller.getViewTreeObserver()
                            .addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener { mediaController.hide() })
                        Log.e(
                            "ERROR TAG",
                            document.data!!["medicinevideo"].toString() + " <- DOES THIS HAVE DATA? VIDEO DETECTED"
                        )

                    }
                } else {
                    Log.e("ERROR TAG", "medicine does not exist!")
                }
            }


        queryFirebase(
            recyclerView,
            prog,
            medid,
            sharedPreference.getValueString("patientspecific")!!,
            titledetails
        )


        return v
    }

    fun queryFirebase(
        recyclerView: RecyclerView,
        prog: ProgressBar,
        medicineid: String,
        documentspecific: String,
        textView: TextView
    ) {
        showLoading(prog)
        val db = FirebaseFirestore.getInstance()

        val instructionlist = ArrayList<instruction>()

        db.collection("PatientList").document(documentspecific).collection("Medicine")
            .document(medicineid).collection("instruction")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (result.size() != 0){
                        textView.setVisibility(View.VISIBLE)

                    }

                    val instructiontitle = document.data["instructiontitle"].toString()
                    val instructiondesc = document.data["instructiondesc"].toString()
                    val instructionimage = document.data["instructionimage"].toString()

                    instructionlist.add(
                        instruction(
                            instructiontitle,
                            instructiondesc,
                            instructionimage
                        )
                    )
                }

                val adapter = medicationinstructionAdapter(instructionlist)
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

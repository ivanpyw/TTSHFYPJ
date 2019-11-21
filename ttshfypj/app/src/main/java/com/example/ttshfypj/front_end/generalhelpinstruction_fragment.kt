package com.example.ttshfypj.front_end


import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.adapters.generalhelpAdapter
import com.example.ttshfypj.adapters.generalinstructionAdapter
import com.example.ttshfypj.data_class.generalinstruction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.generalhelpinstruction_fragment.view.*

/**
 * A simple [Fragment] subclass.
 */
class generalhelpinstruction_fragment : Fragment() {

    companion object {
        var ARG_specificid = "docidspecific"
        var ARG_categorydocid = "docid"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.generalhelpinstruction_fragment, container, false)

        val sharedPreference: SharedPreference = SharedPreference(context!!)
        val recyclerView = v.generalinstructionRecyclerview as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar;
        val generalinstructiontext = v.findViewById(R.id.generalinstructiontitletextview) as TextView;


        val videoViewLandscape = v.findViewById(R.id.videoview_generalinstruction) as VideoView
        val videocardview = v.findViewById(R.id.videocardview) as CardView
        val videotext = v.findViewById(R.id.videotextview) as TextView

        val categoryid = arguments?.getString(generalhelpinstruction_fragment.ARG_categorydocid)!!
        val specificid = arguments?.getString(generalhelpinstruction_fragment.ARG_specificid)!!


        videotext.setVisibility(View.GONE)
        videocardview.setVisibility(View.GONE)
        prog.setVisibility(View.INVISIBLE);
        generalinstructiontext.setVisibility(View.GONE)

        val db = FirebaseFirestore.getInstance()

        db.collection("GeneralInformation").document(categoryid).collection("list")
            .document(specificid)
            .get()
            .addOnSuccessListener { document ->

                if (document != null) {

                    val videolink = document.data!!["specificinstructionvideo"].toString()

                    Log.e(
                        "ERROR TAG",
                        document.data!!["specificinstructionvideo"].toString()
                    )

                    if (videolink.isNullOrBlank()) {
                        Log.e(
                            "ERROR TAG",
                            document.data!!["specificinstructionvideo"].toString() + " <- DOES THIS HAVE DATA? VIDEO NOT DETECTED"
                        )
                    } else {
                        videotext.setVisibility(View.VISIBLE)
                        videocardview.setVisibility(View.VISIBLE)

                        val video = document.data!!["specificinstructionvideo"].toString()

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
                            document.data!!["specificinstructionvideo"].toString() + " <- DOES THIS HAVE DATA? VIDEO DETECTED"
                        )

                    }
                } else {
                    Log.e("ERROR TAG", "medicine does not exist!")
                }
            }

        queryFirebase(
            recyclerView,
            prog,
            arguments?.getString(generalhelpinstruction_fragment.ARG_specificid)!!,
            arguments?.getString(generalhelpinstruction_fragment.ARG_categorydocid)!!,
            generalinstructiontext
        )

        return v
    }


    fun queryFirebase(
        recyclerView: RecyclerView,
        prog: ProgressBar,
        helpid: String,
        categoryid: String,
        textview: TextView
    ) {
        showLoading(prog)
        val db = FirebaseFirestore.getInstance()

        val generalinstructionlist = ArrayList<generalinstruction>()

        db.collection("GeneralInformation").document(categoryid).collection("list").document(helpid)
            .collection("steps")
            .get()
            .addOnSuccessListener { result ->
                /*val listOfSteps : List<String> = result["steps"] as List<String>;
                for ( document in listOfSteps) {*/
                for (document in result) {
                    if(result.size() == 0) {
                        textview.setVisibility(View.VISIBLE)
                    }


                    val instructiontitle = document.data["specificinstructiontitle"].toString()
                    val instructiondesc = document.data["specificinstructiondesc"].toString()
                    val instructionimage = document.data["specificinstructionimage"].toString()

                    generalinstructionlist.add(
                        generalinstruction(
                            instructiontitle,
                            instructiondesc,
                            instructionimage
                        )
                    )
                }

                val adapter = generalinstructionAdapter(generalinstructionlist)
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



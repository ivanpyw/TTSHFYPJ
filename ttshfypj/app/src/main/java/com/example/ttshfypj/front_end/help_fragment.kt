package com.example.ttshfypj.front_end

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.ttshfypj.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class help_fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.help_fragment, container, false)
        val manager = activity?.supportFragmentManager
        val mainGrid = v.findViewById(R.id.mainGrid) as GridLayout
        setSingleEvent(mainGrid)
        return v
    }

    private fun setSingleEvent(mainGrid: GridLayout) {
        //Loop all child item of Main Grid

        var category: String? = null
        for (i in 0 until mainGrid.childCount) {
            //You can see , all child item is CardView , so we just cast object to CardView
            val cardView = mainGrid.getChildAt(i) as CardView
            cardView.setOnClickListener(View.OnClickListener {

                try {
                    if (i == 0) {
                        category = "Contacts"
                    } else if (i == 1) {
                        category = "FAQ"
                    } else if (i == 2) {
                        category = "Diseases"
                    } else if (i == 3) {
                        category = "First Aid Kit"
                    } else if (i == 4) {
                        category = "Medicines"
                    } else if (i == 5) {
                        category = "Emergency"
                    }
                } finally {
                    creategeneralhelp_fragment(category!!)
                }
            })
        }
    }

    fun creategeneralhelp_fragment(category: String) {
        var newFragment = generalhelp_fragment()
        var args = Bundle()
        args.putString(generalhelp_fragment.ARG_categoryhelp, category)
        newFragment.arguments = args
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentplaceholder, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }


}

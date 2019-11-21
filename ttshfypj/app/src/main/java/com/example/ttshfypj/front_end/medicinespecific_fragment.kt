package com.example.ttshfypj.front_end


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast

import com.example.ttshfypj.R

import info.hoang8f.android.segmented.SegmentedGroup
import kotlinx.android.synthetic.main.medicinespecific_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class medicinespecific_fragment : Fragment() {

    companion object {
        var ARG_medicinedatabaseid = "userinput"
    }

    /* private val NavListener = medicinespecificnavbar.OnNavigationItemSelectedListener { item ->
         when (item.itemId) {
             R.id.medicinenavigation_Details -> {
                 createMedicineDetailFragment()
                 return@OnNavigationItemSelectedListener true
             }

         }
         false
     }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.medicinespecific_fragment, container, false)

        val medicinespecificnavbar = v.findViewById(R.id.medicinespecificnavbar) as SegmentedGroup
        medicinespecificnavbar.setTintColor(
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#758BFD")
        )

        createMedicineDetailFragment()

        val DetailsButton = v.findViewById(R.id.DetailsButton) as RadioButton
        DetailsButton.setOnClickListener {
            createMedicineDetailFragment()
        }
        val GuideButton = v.findViewById(R.id.GuideButton) as RadioButton
        GuideButton.setOnClickListener {
            createMedicineInstructionFragment()
        }

        return v
    }

    fun createMedicineDetailFragment() {
        val medicinedata: medicinedetails_fragment? =
            activity?.supportFragmentManager?.findFragmentByTag("medicinedetails_fragment") as medicinedetails_fragment?
        if (medicinedata == null) {
            var newFragment = medicinedetails_fragment()
            var args = Bundle()
            args.putString(
                medicinedetails_fragment.ARG_medicineid,
                arguments?.getString(medicinespecific_fragment.ARG_medicinedatabaseid)
            )
            newFragment.arguments = args

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.medicinefragmentplaceholder, newFragment)
            transaction?.addToBackStack("medicinedetail")
            transaction?.commit()
        }


    }

    fun createMedicineInstructionFragment() {
        val medicinedata: medicineinstructions_fragment? =
            activity?.supportFragmentManager?.findFragmentByTag("medicineinstructions_fragment") as medicineinstructions_fragment?
        if (medicinedata == null) {
            var newFragment = medicineinstructions_fragment()
            var args = Bundle()
            args.putString(
                medicineinstructions_fragment.ARG_medicineid,
                arguments?.getString(medicinespecific_fragment.ARG_medicinedatabaseid)
            )
            newFragment.arguments = args

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.medicinefragmentplaceholder, newFragment)
            transaction?.addToBackStack("medicineinstruction")
            transaction?.commit()
        }


    }

}

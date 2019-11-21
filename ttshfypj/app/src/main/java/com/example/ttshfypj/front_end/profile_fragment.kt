package com.example.ttshfypj.front_end


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.onetimeloginActivity
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class profile_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreference: SharedPreference = SharedPreference(context!!)
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.profile_fragment, container, false)

        val idofpatient = v.findViewById(R.id.idtextview) as TextView
        val logout = v.findViewById(R.id.logoutbutton) as Button;
        val carecentre = v.findViewById(R.id.carecentertextview) as TextView

        idofpatient.text = sharedPreference.getValueString("code")!!
        carecentre.text = sharedPreference.getValueString("patientcarecentre")!!
        logout.setOnClickListener {

            sharedPreference.clearSharedPreference()
            val intent = Intent(context!!, onetimeloginActivity::class.java)
            // start your next activity
            startActivity(intent)
            activity?.finish()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        return v
    }


}

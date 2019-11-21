package com.example.ttshfypj.front_end


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ttshfypj.R
import com.example.ttshfypj.SharedPreference
import com.example.ttshfypj.adapters.generalhelpAdapter
import com.example.ttshfypj.data_class.generalhelpfirstlayer
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.generalhelp_fragment.view.*
import kotlinx.android.synthetic.main.generalhelp_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class generalhelp_fragment : Fragment() {

    companion object {
        var ARG_categoryhelp = "category"
    }

    val categorydocid: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.generalhelp_fragment, container, false)
        val searchbar = v.findViewById(R.id.Searchbarinput) as EditText

        val recyclerView = v.generalhelp_recyclerview as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val prog = v.findViewById(R.id.progressCircle) as ProgressBar;
        prog.setVisibility(View.INVISIBLE);
        queryFirebase(recyclerView, prog)

        searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()){
                    queryFirebase(recyclerView, prog)
                } else {
                    queryFirebaseSearch(recyclerView, prog, searchbar.text.toString())

                }

            }


        })



        return v
    }


    private fun onItemClickHandler(position: String) {

        val db = FirebaseFirestore.getInstance()
        db.collection("GeneralInformation")
            .whereEqualTo(
                "Category",
                arguments?.getString(ARG_categoryhelp)
            )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val docid = document.id

                    var generalhelpinstruction: generalhelpinstruction_fragment? =
                        activity?.supportFragmentManager?.findFragmentByTag("generalhelpinstruction_fragment") as generalhelpinstruction_fragment?
                    if (generalhelpinstruction == null) {
                        val newFragment = generalhelpinstruction_fragment()
                        val args = Bundle()
                        args.putString(generalhelpinstruction_fragment.ARG_specificid, position)
                        args.putString(generalhelpinstruction_fragment.ARG_categorydocid, docid)
                        newFragment.arguments = args

                        var ft = activity?.supportFragmentManager?.beginTransaction()
                        ft?.replace(R.id.fragmentplaceholder, newFragment, null)
                        ft?.commit()

                    }

                }

            }


    }

    fun queryFirebase(recyclerView: RecyclerView, prog: ProgressBar) {
        showLoading(prog)
        val db = FirebaseFirestore.getInstance()
        val generalhelplist = ArrayList<generalhelpfirstlayer>()

        db.collection("GeneralInformation")
            .whereEqualTo(
                "Category",
                arguments?.getString(ARG_categoryhelp)
            )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val docid = document.id
                    db.collection("GeneralInformation").document(docid).collection("list")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val helptitle = document.data["helptitle"].toString()
                                val helpdescription = document.data["helpdescription"].toString()
                                val helpimage = document.data["helpimage"].toString()
                                val helpdocid = document.id

                                generalhelplist.add(
                                    generalhelpfirstlayer(
                                        helptitle.capitalize(), helpdescription.capitalize(), helpimage, helpdocid
                                    )
                                )
                            }

                            val adapter = generalhelpAdapter(
                                generalhelplist,
                                this@generalhelp_fragment::onItemClickHandler
                            )
                            recyclerView.adapter = adapter
                            hideLoading(prog)
                        }
                }
            }
    }

    fun queryFirebaseSearch(recyclerView: RecyclerView, prog: ProgressBar, search: String) {
        showLoading(prog)
        val db = FirebaseFirestore.getInstance()
        val generalhelplist = ArrayList<generalhelpfirstlayer>()

        db.collection("GeneralInformation")
            .whereEqualTo(
                "Category",
                arguments?.getString(ARG_categoryhelp)
            )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val docid = document.id

                    db.collection("GeneralInformation").document(docid).collection("list")
                        .orderBy("helptitle")
                        .startAt(search.toLowerCase())
                        .endAt(search.toLowerCase()+"\uf8ff")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val helptitle = document.data["helptitle"].toString()
                                val helpdescription = document.data["helpdescription"].toString()
                                val helpimage = document.data["helpimage"].toString()
                                val helpdocid = document.id

                                generalhelplist.add(
                                    generalhelpfirstlayer(
                                        helptitle.capitalize(), helpdescription.capitalize() , helpimage, helpdocid
                                    )
                                )
                            }

                            val adapter = generalhelpAdapter(
                                generalhelplist,
                                this@generalhelp_fragment::onItemClickHandler
                            )
                            recyclerView.adapter = adapter
                            hideLoading(prog)
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






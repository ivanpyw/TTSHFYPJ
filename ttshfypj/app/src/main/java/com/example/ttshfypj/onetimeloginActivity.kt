@file:Suppress("SENSELESS_COMPARISON")

package com.example.ttshfypj

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.onetimelogin.*

import android.net.wifi.WifiManager


class onetimeloginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onetimelogin)

        val wifi = getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifi.isWifiEnabled = true // true or false to activate/deactivate wifi

        val sharedPreference: SharedPreference = SharedPreference(this)

        val ptCode = findViewById(R.id.Patientcode) as EditText


        BtnLogin.setOnClickListener {

            val inputcode = ptCode.text.toString()

            if (!inputcode.isNullOrBlank()) {
                val db = FirebaseFirestore.getInstance()

                db.collection("PatientList")
                    .whereEqualTo("patientid", inputcode)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) {
                            ptCode.setError("User does not exist")
                        } else {
                            for (document in result) {
                                sharedPreference.save("patientspecific", document.id)
                                sharedPreference.save(
                                    "patientcarecentre",
                                    document.data["carecentre"].toString()
                                )
                                sharedPreference.save("code", document.data["patientid"].toString())

                                val intent = Intent(this, MainActivity::class.java)
                                // start your next activity
                                startActivity(intent)
                                finish()
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            }
                        }
                    }
            } else {
                ptCode.setError("Please Enter Something!")
            }
        }
    }
}

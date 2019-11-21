package com.example.ttshfypj


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference: SharedPreference = SharedPreference(this)
        val db = FirebaseFirestore.getInstance()

        if (sharedPreference.getValueString("code") != null) {

            Log.e("patientspecific", sharedPreference.getValueString("patientspecific")!!)
            Log.e("patientcode", sharedPreference.getValueString("code")!!)


            val intent = Intent(this, MainActivity::class.java)
            // start your next activity
            startActivity(intent)
            finish()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {
            val intent = Intent(this, onetimeloginActivity::class.java)
            startActivity(intent)
            finish()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}


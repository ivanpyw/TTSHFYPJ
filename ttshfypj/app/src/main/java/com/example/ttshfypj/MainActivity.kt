package com.example.ttshfypj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.example.ttshfypj.front_end.help_fragment
import com.example.ttshfypj.front_end.medications_fragment
import com.example.ttshfypj.front_end.profile_fragment
import com.example.ttshfypj.front_end.reminder_fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_reminders -> {
                    createReminderFragment()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_medication -> {
                    createMedicationFragment()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_help -> {
                    createHelpFragment()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    createProfileFragment()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setItemIconTintList(null);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        createReminderFragment()


    }

    fun createReminderFragment() {
        val transaction = manager.beginTransaction()
        val fragment = reminder_fragment()
        transaction.replace(R.id.fragmentplaceholder, fragment)
        transaction.addToBackStack("reminder")
        transaction.commit()
    }

    fun createMedicationFragment() {
        val transaction = manager.beginTransaction()
        val fragment = medications_fragment()
        transaction.replace(R.id.fragmentplaceholder, fragment)
        transaction.addToBackStack("medication")
        transaction.commit()
    }

    fun createHelpFragment() {
        val transaction = manager.beginTransaction()
        val fragment = help_fragment()
        transaction.replace(R.id.fragmentplaceholder, fragment)
        transaction.addToBackStack("help")
        transaction.commit()
    }

    fun createProfileFragment() {
        val transaction = manager.beginTransaction()
        val fragment = profile_fragment()
        transaction.replace(R.id.fragmentplaceholder, fragment)
        transaction.addToBackStack("profile")
        transaction.commit()
    }


}


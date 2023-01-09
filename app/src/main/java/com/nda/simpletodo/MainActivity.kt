package com.nda.simpletodo

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nda.simpletodo.ui.home.HomeFragment
import com.nda.simpletodo.ui.searchNotes.SearchNoteFragment
import com.nda.simpletodo.ui.setting.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val calendarFragment = SearchNoteFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(UtilsManager.getEnablePassLock() == true)
        {
            dialogPassLock();
        }

        if (UtilsManager.getApplicationLanguage().toString().isEmpty())
        {
            UtilsManager.setApplicationLanguage(getString(R.string.applicationLanguage_english))
            UtilsManager.setLocalLanguage(this,"en")
        }
        else if (UtilsManager.getApplicationLanguage().toString().trim() == getString(R.string.applicationLanguage_vietNam))
        {
            UtilsManager.setLocalLanguage(this,"vi-rVN")
        }
        else if (UtilsManager.getApplicationLanguage().toString().trim() == getString(R.string.applicationLanguage_english))
        {
            UtilsManager.setLocalLanguage(this,"en")
        }

        replaceFragment(homeFragment)

        bottom_nav_view.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.navigation_home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.navigation_calendar -> {
                    replaceFragment(calendarFragment)
                    true
                }
                R.id.navigation_setting -> {
                    replaceFragment(settingsFragment)
                    true
                }
                else -> {
                    replaceFragment(homeFragment)
                    true
                }
            }
        }
    }

    private fun replaceFragment(replaceFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_host_activity_main, replaceFragment)
                .commit()
        }
    }

    private fun dialogPassLock() {
        val dialog = Dialog(this@MainActivity)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_passlock)
        dialog.setCancelable(false)
        val edt_pass = dialog.findViewById<EditText>(R.id.edt_pass)
        val btn_setPassLock = dialog.findViewById<Button>(R.id.btn_setPassLock)
        btn_setPassLock.setOnClickListener(View.OnClickListener {
            val passLock = edt_pass.text.toString().trim { it <= ' ' }
            if (passLock == UtilsManager.getPassLock()) {
                dialog.dismiss()
//                startActivity(Intent(this@MainActivity, MainActivity::class.java))
//                finishAffinity()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Error : Invalid passwod",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
        })
        dialog.show()
    }


}
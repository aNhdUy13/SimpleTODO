package com.nda.simpletodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nda.simpletodo.ui.searchNotes.SearchNoteFragment
import com.nda.simpletodo.ui.home.HomeFragment
import com.nda.simpletodo.ui.setting.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val calendarFragment = SearchNoteFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
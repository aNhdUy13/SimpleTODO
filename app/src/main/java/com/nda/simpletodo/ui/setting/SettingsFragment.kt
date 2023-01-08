package com.nda.simpletodo.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.nda.simpletodo.R
import com.nda.simpletodo.UtilsManager

class SettingsFragment : Fragment() {

    var switch_passlock: SwitchCompat? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initUI(view)
        init()
        return view
    }

    private fun init() {
        /**
         * Pass lock
         */
        switch_passlock!!.isChecked = UtilsManager.getEnablePassLock() == true

        switch_passlock!!.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // when switch is checked
                UtilsManager.setEnablePassLock(true)
                switch_passlock!!.isChecked = true

            } else {
                // when switch is un-checked
                UtilsManager.setEnablePassLock(false)
                switch_passlock!!.isChecked = false
            }
        }

    }

    private fun initUI(view: View?) {
        switch_passlock = view?.findViewById(R.id.switch_passlock)

    }


}
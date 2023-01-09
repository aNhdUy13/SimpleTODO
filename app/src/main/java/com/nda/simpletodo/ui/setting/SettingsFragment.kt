package com.nda.simpletodo.ui.setting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
                dialogSetPassLock()

            } else {
                // when switch is un-checked
                dialogVerifyPasslock()
            }
        }

    }

    private fun initUI(view: View?) {
        switch_passlock = view?.findViewById(R.id.switch_passlock)

    }

    private fun dialogSetPassLock() {
        val dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_set_verify_passlock)
        dialog.setCancelable(false)
        val edt_pass = dialog.findViewById<EditText>(R.id.edt_pass)
        val btn_setPassLock = dialog.findViewById<Button>(R.id.btn_setPassLock)
        // Button btn_close = dialog.findViewById(R.id.btn_close);
        btn_setPassLock.setOnClickListener(View.OnClickListener {
            val passLock = edt_pass.text.toString().trim()
            if (passLock.isEmpty()) {
                Toast.makeText(context, "Error : Fulfill data", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            UtilsManager.setPassLock(passLock)
            UtilsManager.setEnablePassLock(true)
            switch_passlock!!.isChecked = true
            dialog.dismiss()
        })

//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // When user CANCEL with ENABLE pass lock
//                // so pass lock continue = false
//                DataLocalManager.setEnablePassLock(false);
//                switch_passlock.setChecked(false);
//
//                dialog.dismiss();
//            }
//        });
        dialog.show()
    }


    private fun dialogVerifyPasslock()
    {
        var dialog = Dialog(requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_set_verify_passlock)

        val txt_titlePasslock = dialog.findViewById<TextView>(R.id.txt_titlePasslock)
        val edt_pass = dialog.findViewById<EditText>(R.id.edt_pass)
        val btn_setPassLock = dialog.findViewById<Button>(R.id.btn_setPassLock)
        // Button btn_close = dialog.findViewById(R.id.btn_close);

        // Button btn_close = dialog.findViewById(R.id.btn_close);
        txt_titlePasslock.text = "Verify Passlock"
        btn_setPassLock.text = "Verify"
        btn_setPassLock.setOnClickListener(View.OnClickListener {

            val passLock = edt_pass.text.toString().trim()
            if (passLock == UtilsManager.getPassLock())
            {
                UtilsManager.setEnablePassLock(false)
                switch_passlock!!.isChecked = false
                dialog.dismiss()
            }
            else {
                Toast.makeText(context, "Error : Mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
        })

        dialog.show()
    }
}
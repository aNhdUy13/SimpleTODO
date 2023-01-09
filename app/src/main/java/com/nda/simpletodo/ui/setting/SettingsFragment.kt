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
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nda.simpletodo.R
import com.nda.simpletodo.UtilsManager
import kotlinx.android.synthetic.main.bottomsheet_applicaiton_language.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    var actionOnClick: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        /**
         * Process raw data
         */
        switch_passlock!!.isChecked = UtilsManager.getEnablePassLock() == true

        txt_applicationLanguage.text = UtilsManager.getApplicationLanguage().toString()



        switch_passlock!!.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // when switch is checked
                dialogSetPassLock()

            } else {
                // when switch is un-checked
                dialogVerifyPasslock()
            }
        }


        actionOnClick = View.OnClickListener {
            if (it == ll_applicationLanguage)
            {
                bottomSheetSelectApplicationLanguage()
            }
        }
        ll_applicationLanguage.setOnClickListener(actionOnClick)
    }

    private fun bottomSheetSelectApplicationLanguage() {
        val view: View = layoutInflater.inflate(R.layout.bottomsheet_applicaiton_language, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.txt_english.setOnClickListener {
            txt_applicationLanguage.text = bottomSheetDialog.txt_english.text.toString().trim()
            UtilsManager.setApplicationLanguage(getString(R.string.applicationLanguage_english))
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.txt_vietNam.setOnClickListener {
            txt_applicationLanguage.text = bottomSheetDialog.txt_vietNam.text.toString().trim()
            UtilsManager.setApplicationLanguage(getString(R.string.applicationLanguage_vietNam))
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
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
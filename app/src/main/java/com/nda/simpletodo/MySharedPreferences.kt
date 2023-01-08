package com.nda.simpletodo

import android.content.Context

class MySharedPreferences {
    private val MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES"


    private var mContext: Context?

    constructor(mContext: Context) {
        this.mContext = mContext
    }


    /*****************************************
     *
     *  (Frame) Boolean value
     *
     ****************************************/
    fun putBooleanValue(value: Boolean, key: String) {
        val sharedPreferences = mContext?.getSharedPreferences(
            MY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBooleanValue(key: String): Boolean? {
        val sharedPreferences = mContext?.getSharedPreferences(
            MY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedPreferences?.getBoolean(key, false)
    }


    /*****************************************
     *
     *  (Frame) String value
     *
     ****************************************/
    fun putStringValue(value: String?, key: String?) {
        val sharedPreferences = mContext!!.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key: String?): String? {
        val sharedPreferences = mContext!!.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }


}
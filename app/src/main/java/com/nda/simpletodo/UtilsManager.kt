package com.nda.simpletodo

import android.content.Context

class UtilsManager {

    var  mySharedPreferences: MySharedPreferences? = null

    companion object{
        private val PREF_PASSLOCK = "PREF_PASSLOCK"
        private val PREF_ENABLE_PASSLOCK = "PREF_ENABLE_PASSLOCK"

        lateinit var instance: UtilsManager

        fun init(context: Context) {
            instance = UtilsManager()
            instance.mySharedPreferences = MySharedPreferences(context)
        }

        @JvmName("getInstance1")
        fun getMyInstance(): UtilsManager{
            if (instance == null) {
                instance = UtilsManager()
            }

            return instance as UtilsManager
        }


        /**
         * Related to set enable pass lock
         */
        fun setEnablePassLock(isEnable: Boolean) {
            getMyInstance().mySharedPreferences?.putBooleanValue(isEnable, PREF_ENABLE_PASSLOCK)
        }

        fun getEnablePassLock(): Boolean? {
            return getMyInstance().mySharedPreferences?.getBooleanValue(PREF_ENABLE_PASSLOCK)
        }

        fun setPassLock(value: String?) {
            getMyInstance().mySharedPreferences?.putStringValue(value, PREF_PASSLOCK)
        }

        fun getPassLock(): String? {
            return getMyInstance().mySharedPreferences?.getStringValue(PREF_PASSLOCK)
        }

        fun formatMonthToStr(month: String, day: String, year: String): String? {
            var strFormatted = "Unk"

            if (month == "01") {
                strFormatted = "January"
            } else if (month == "02") {
                strFormatted = "February"
            } else if (month == "03") {
                strFormatted = "March"
            } else if (month == "04") {
                strFormatted = "April"
            } else if (month == "05") {
                strFormatted = "May"
            } else if (month == "06") {
                strFormatted = "June"
            } else if (month == "07") {
                strFormatted = "July"
            } else if (month == "08") {
                strFormatted = "August"
            } else if (month == "09") {
                strFormatted = "September"
            } else if (month == "10") {
                strFormatted = "October"
            } else if (month == "11") {
                strFormatted = "November"
            } else if (month == "12") {
                strFormatted = "December"
            }

            return "$strFormatted $day, $year"
        }

    }



}
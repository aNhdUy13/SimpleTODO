package com.nda.simpletodo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.google.android.material.internal.ContextUtils
import java.util.*

class UtilsManager {

    var  mySharedPreferences: MySharedPreferences? = null

    companion object{
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
         * Related to Application language
         */
        fun setApplicationLanguage(value: String)
        {
            getMyInstance().mySharedPreferences?.putStringValue("APPLICATION_LANGUAGE", value)
        }

        fun getApplicationLanguage(): String? {
            return  getMyInstance().mySharedPreferences?.getStringValue("APPLICATION_LANGUAGE")
        }

        /**
         * Related to set enable pass lock
         */
        fun setEnablePassLock(isEnable: Boolean) {
            getMyInstance().mySharedPreferences?.putBooleanValue(isEnable, "ENABLE_PASSLOCK")
        }

        fun getEnablePassLock(): Boolean? {
            return getMyInstance().mySharedPreferences?.getBooleanValue("ENABLE_PASSLOCK")
        }

        fun setPassLock(value: String?) {
            getMyInstance().mySharedPreferences?.putStringValue("PASSLOCK", value)
        }

        fun getPassLock(): String? {
            return getMyInstance().mySharedPreferences?.getStringValue("PASSLOCK")
        }

        /**
         *
         * */
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

        /**
         *
         * */
        fun setLocalLanguage(activity: Activity, languageCode: String)
        {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val resources: Resources = activity.resources
            val configuration: Configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }


    }


}
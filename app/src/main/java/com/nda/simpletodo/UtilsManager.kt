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

    }



}
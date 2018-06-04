package com.orieange.stock.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by GuoJinyu on 2015/11/16.
 */
class PreferenceUtil private constructor(context: Context) {
    companion object {
        private var singleIns: PreferenceUtil? = null

        fun getSingleton(context: Context): PreferenceUtil? {
            if (singleIns == null) {
                singleIns = PreferenceUtil(context)
            }
            return singleIns
        }
    }

    private val mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveBoolean(key: String, value: Boolean?) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value!!)
        editor.commit()
    }

    fun getBoolean(key: String): Boolean? {
        return mSharedPreferences.getBoolean(key, false)
    }

    fun getBoolean(key: String, def: Boolean): Boolean? {
        return mSharedPreferences.getBoolean(key, def)
    }

    fun saveInt(key: String, value: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String): Int {
        return mSharedPreferences.getInt(key, 0)
    }

    fun getInt(key: String, def: Int): Int {
        return mSharedPreferences.getInt(key, def)
    }
}

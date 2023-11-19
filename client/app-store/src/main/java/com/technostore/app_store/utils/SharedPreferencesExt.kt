package com.technostore.app_store.utils

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun SharedPreferences.booleanPref(
    key: String,
    defValue: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return this@booleanPref.getBoolean(key, defValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            this@booleanPref.edit().putBoolean(key, value).apply()
        }
    }
}

fun SharedPreferences.stringNullablePref(
    key: String,
    defValue: String? = null
): ReadWriteProperty<Any, String?> {
    return object : ReadWriteProperty<Any, String?> {

        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return this@stringNullablePref.getString(key, defValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
            if (value == null) {
                this@stringNullablePref.edit().remove(key).apply()
            } else {
                this@stringNullablePref.edit().putString(key, value).apply()
            }
        }
    }
}

fun SharedPreferences.longPref(
    key: String,
    defValue: Long = 0
): ReadWriteProperty<Any, Long> {
    return object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return this@longPref.getLong(key, defValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            this@longPref.edit().putLong(key, value).apply()
        }
    }
}

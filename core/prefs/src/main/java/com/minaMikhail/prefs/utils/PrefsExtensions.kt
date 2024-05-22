package com.minaMikhail.prefs.utils

import android.content.SharedPreferences
import com.minaMikhail.prefs.enums.PreferencesKey

operator fun <T : Any> SharedPreferences.set(key: PreferencesKey, value: T?) {
    when (value) {
        is String -> editPref { it.putString(key.name, value) }
        is Int -> editPref { it.putInt(key.name, value) }
        is Boolean -> editPref { it.putBoolean(key.name, value) }
        is Float -> editPref { it.putFloat(key.name, value) }
        is Long -> editPref { it.putLong(key.name, value) }

        else -> throw UnsupportedOperationException(
            "${key.name} is: ${value?.javaClass}, This data type not yet implemented in: ${this.javaClass}"
        )
    }
}

inline operator fun <reified T : Any> SharedPreferences.get(
    key: PreferencesKey,
    defaultValue: T
): T {
    return when (defaultValue) {
        is String -> getString(key.name, defaultValue as String) as T
        is Int -> getInt(key.name, defaultValue as Int) as T
        is Boolean -> getBoolean(key.name, defaultValue as Boolean) as T
        is Float -> getFloat(key.name, defaultValue as Float) as T
        is Long -> getLong(key.name, defaultValue as Long) as T

        else -> throw UnsupportedOperationException(
            "${key.name} is: ${defaultValue.javaClass}, This data type not yet implemented in: ${this.javaClass}"
        )
    }
}

inline fun SharedPreferences.editPref(operation: (SharedPreferences.Editor) -> Unit) {
    with(this.edit()) {
        operation(this)
        apply()
    }
}

fun SharedPreferences.removePref(key: PreferencesKey) {
    with(this.edit()) {
        remove(key.name)
        apply()
    }
}

fun SharedPreferences.clear() {
    with(this.edit()) {
        clear()
        apply()
    }
}
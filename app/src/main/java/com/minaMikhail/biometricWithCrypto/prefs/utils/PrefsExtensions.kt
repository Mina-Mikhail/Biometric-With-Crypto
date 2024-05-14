package com.minaMikhail.biometricWithCrypto.prefs.utils

import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import com.minaMikhail.biometricWithCrypto.prefs.enums.PreferencesKey
import java.io.Serializable

operator fun <T : Any> SharedPreferences.set(key: PreferencesKey, value: T?) {
    when (value) {
        is String -> editPref { it.putString(key.name, value) }
        is Int -> editPref { it.putInt(key.name, value) }
        is Boolean -> editPref { it.putBoolean(key.name, value) }
        is Float -> editPref { it.putFloat(key.name, value) }
        is Long -> editPref { it.putLong(key.name, value) }
        is Serializable -> editPref { it.putString(key.name, Gson().toJson(value)) }
        is Parcelable -> editPref { it.putString(key.name, Gson().toJson(value)) }
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
        is Serializable -> Gson().fromJson(
            getString(key.name, defaultValue as String),
            T::class.java
        )

        is Parcelable -> Gson().fromJson(getString(key.name, defaultValue as String), T::class.java)
        else -> throw UnsupportedOperationException(
            "${key.name} is: ${defaultValue.javaClass}, This data type not yet implemented in: ${this.javaClass}"
        )
    }
}

inline operator fun <reified T : Any> SharedPreferences.get(
    key: PreferencesKey,
    defaultValue: Class<T>
): T? {
    return when (T::class) {
        Serializable::class -> {
            Gson().fromJson(getString(key.name, ""), defaultValue)
        }

        else -> defaultValue as? T
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
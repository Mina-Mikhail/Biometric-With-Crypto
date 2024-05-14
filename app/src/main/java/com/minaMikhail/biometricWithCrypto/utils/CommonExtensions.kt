package com.minaMikhail.biometricWithCrypto.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Any?.toJsonString(): String? {
    return if (this == null) {
        null
    } else {
        Gson().toJson(this)
    }
}

fun <T : Any> String?.toJsonModel(modelClass: Class<T>): T? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        Gson().fromJson(this, modelClass)
    }
}

inline fun <reified T> String?.toJsonList(): T? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        Gson().fromJson(this, object : TypeToken<T>() {}.type)
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

fun View.showSnackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

fun Context.getStringFromResources(@StringRes resId: Int): String {
    return ContextCompat.getString(this, resId)
}
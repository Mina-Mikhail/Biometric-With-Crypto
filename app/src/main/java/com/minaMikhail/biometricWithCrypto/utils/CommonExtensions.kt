package com.minaMikhail.biometricWithCrypto.utils

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

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
package com.minaMikhail.biometricWithCrypto.prefs

import android.content.SharedPreferences
import com.minaMikhail.biometricWithCrypto.prefs.enums.PreferencesKey
import com.minaMikhail.biometricWithCrypto.prefs.utils.get
import com.minaMikhail.biometricWithCrypto.prefs.utils.set
import javax.inject.Inject

class PrefsProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : IPrefsProvider {

    override fun saveString(
        key: PreferencesKey,
        data: String?
    ) {
        sharedPreferences[key] = data
    }

    override fun getString(
        key: PreferencesKey
    ): String = sharedPreferences[key, ""]
}
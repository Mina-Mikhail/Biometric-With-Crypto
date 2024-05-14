package com.minaMikhail.biometricWithCrypto.prefs

import com.minaMikhail.biometricWithCrypto.prefs.enums.PreferencesKey

interface IPrefsProvider {

    fun saveString(
        key: PreferencesKey,
        data: String?
    )

    fun getString(
        key: PreferencesKey
    ): String
}
package com.minaMikhail.prefs

import com.minaMikhail.prefs.enums.PreferencesKey

interface IPrefsProvider {

    fun saveString(
        key: PreferencesKey,
        data: String?
    )

    fun getString(
        key: PreferencesKey
    ): String
}
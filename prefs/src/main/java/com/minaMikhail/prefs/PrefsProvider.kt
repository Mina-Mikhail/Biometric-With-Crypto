package com.minaMikhail.prefs

import android.content.SharedPreferences
import com.minaMikhail.prefs.enums.PreferencesKey
import com.minaMikhail.prefs.utils.clear
import com.minaMikhail.prefs.utils.get
import com.minaMikhail.prefs.utils.set
import javax.inject.Inject

class PrefsProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : IPrefsProvider {

    override fun saveEncryptedData(data: String?) {
        sharedPreferences[PreferencesKey.ENCRYPTED_DATA_KEY] = data
    }

    override fun getEncryptedData(): String =
        sharedPreferences[PreferencesKey.ENCRYPTED_DATA_KEY, ""]

    override fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences[PreferencesKey.IS_LOGGED_IN] = isLoggedIn
    }

    override fun isLoggedIn(): Boolean = sharedPreferences[PreferencesKey.IS_LOGGED_IN, false]

    override fun clearUserData() {
        sharedPreferences.clear()
    }
}
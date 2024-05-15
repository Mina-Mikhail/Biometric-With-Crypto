package com.minaMikhail.prefs

interface IPrefsProvider {

    fun saveEncryptedData(data: String?)

    fun getEncryptedData(): String

    fun setLoggedIn(isLoggedIn: Boolean)

    fun isLoggedIn(): Boolean

    fun clearUserData()
}
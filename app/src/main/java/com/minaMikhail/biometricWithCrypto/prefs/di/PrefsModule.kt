package com.minaMikhail.biometricWithCrypto.prefs.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.minaMikhail.biometricWithCrypto.prefs.IPrefsProvider
import com.minaMikhail.biometricWithCrypto.prefs.PrefsProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidingPrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = EncryptedSharedPreferences.create(
        "CRYPTO_APP_PREFS",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingPrefsModule {

    @Binds
    @Singleton
    abstract fun bindBiometricProvider(
        prefsProvider: PrefsProvider
    ): IPrefsProvider
}